package com.substring.foodies.controller;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
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


    @PostMapping("/{orderId}")
    public ResponseEntity<?> createPayment(@PathVariable int orderId) throws RazorpayException {

        Order order = orderRepository.findById(orderId).orElseThrow(()->new ResourceNotFound("Order Not Found"));

        RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", order.getTotalAmount() * 100); // Amount is in currency subunits.
        orderRequest.put("currency","INR");
        orderRequest.put("receipt", order.getId());

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
