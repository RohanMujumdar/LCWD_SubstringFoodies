package com.substring.foodies.entity;

import com.substring.foodies.dto.enums.OrderStatus;
import com.substring.foodies.dto.enums.PaymentMode;
import com.substring.foodies.dto.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Data
@Table(name = "food_Orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    private int totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PLACED;

    private LocalDateTime orderedAt;

    private LocalDateTime deliveryTime;

    @ManyToOne
    @JoinColumn(name = "delivery_boy_id")
    private User deliveryBoy;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItemList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private PaymentMode paymentMode;

    private String paymentId;

    private String razorpayId;
}
