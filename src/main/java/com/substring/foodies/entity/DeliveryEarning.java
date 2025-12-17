package com.substring.foodies.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Data
public class DeliveryEarning {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "delivery_boy_id")
    private User deliveryBoy;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private int amount;

    private LocalDateTime deliveryTime;

}
