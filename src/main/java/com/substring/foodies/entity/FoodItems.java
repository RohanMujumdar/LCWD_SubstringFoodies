package com.substring.foodies.entity;
import com.substring.foodies.dto.enums.FoodType;
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
public class FoodItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private int price;
    private boolean isAvailable;

    @Enumerated(EnumType.STRING)
    private FoodType foodType = FoodType.VEG;

    private String imageUrl;
    private LocalDateTime localDateTime;
    private int discountAmount;

    @ManyToOne
    private Restaurant restaurant;

    @PrePersist
    protected void onCreate()
    {
        localDateTime = LocalDateTime.now();
    }

    public int actualPrice()
    {
        return price - discountAmount;
    }

    public int getDiscountPercentage()
    {
        return (discountAmount / price) * 100;
    }
}
