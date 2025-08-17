package com.substring.foodies.controller;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import com.substring.foodies.dto.PaymentVerifyObject;
import com.substring.foodies.dto.enums.PaymentStatus;
import com.substring.foodies.entity.Order;
import com.substring.foodies.exception.ResourceNotFound;
import com.substring.foodies.repository.OrderRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/payment")
// Put the Url of your front-end in here, it will help you resolve the CORS issue.
// @CrossOrigin
public class PaymentController {

    @Value("${razorpay.key_id}")
    private String razorpayKeyId;

    @Value("${razorpay.key_secret}")
    private String razorpayKeySecret;

    @Autowired
    OrderRepository orderRepository;


    @PostMapping("/verify/{orderId}")
    public ResponseEntity<?> verifyPayment(@PathVariable int orderId,
                                           @RequestBody PaymentVerifyObject paymentVerifyObject) throws RazorpayException {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFound("Order not found with Id: " + orderId));
        String paymentOrderId = order.getPaymentId();

        String payload = paymentOrderId + '|' + paymentVerifyObject.getRazorpayPaymentId();
        RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

        JSONObject options = new JSONObject();
        options.put("razorpay_order_id", order.getRazorpayId());
        options.put("razorpay_payment_id", paymentVerifyObject.getRazorpayPaymentId());
        options.put("razorpay_signature", paymentVerifyObject.getRazorpaySignature());

        boolean isVerified =  Utils.verifyPaymentSignature(options, razorpayKeySecret);
        if(isVerified)
        {
            order.setPaymentStatus(PaymentStatus.PAID);
            order.setPaymentId(paymentVerifyObject.getRazorpayPaymentId());
            orderRepository.save(order);
            return ResponseEntity.ok(Map.of("message", "Payment Verified Successfully"));
        }
        else {
            order.setPaymentStatus(PaymentStatus.FAILED);
            orderRepository.save(order);
            return ResponseEntity.ok(Map.of("message", "Payment Verification Failed"));
        }
    }


    @PostMapping("/{orderId}")
    public ResponseEntity<?> createPayment(@PathVariable int orderId) throws RazorpayException {

        Order order = orderRepository.findById(orderId).orElseThrow(()->new ResourceNotFound("Order Not Found"));

        RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", order.getTotalAmount() * 100); // Amount is in currency subunits.
        orderRequest.put("currency","INR");
        orderRequest.put("receipt", String.valueOf(order.getId()));
        JSONObject noteObject = new JSONObject();
        noteObject.put("about", "This is payment done by " + order.getUser().getName());
        orderRequest.put("notes", noteObject);

        com.razorpay.Order razorpay = razorpayClient.orders.create(orderRequest);

        Map<String, Object> response = Map.of(
                "id", razorpay.get("id"),
                "currency", razorpay.get("currency"),
                "amount", razorpay.get("amount"),
                "status", razorpay.get("status"),
                "orderId", order.getId()
        );

        order.setRazorpayId(razorpay.get("id"));
        orderRepository.save(order);
        return ResponseEntity.ok(response);
    }
}
