package com.substring.foodies.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Builder
public class Cart {

    @Id
    private String id;

    @ManyToOne
    private Restaurant restaurant;

    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User creator;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItems> cartItems;

    @PrePersist
    protected void onCreate()
    {
        createdAt = LocalDateTime.now();
    }
}
