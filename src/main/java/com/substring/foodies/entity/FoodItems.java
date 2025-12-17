package com.substring.foodies.entity;
import com.substring.foodies.dto.enums.FoodType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Data
public class FoodItems {

    @Id
    private String id;

    private String name;
    private String description;
    private int price;
    private boolean isAvailable;

    @Enumerated(EnumType.STRING)
    private FoodType foodType = FoodType.VEG;

    private String imageUrl;
    private LocalDateTime localDateTime;
    private int discountAmount;

    @ManyToMany(mappedBy = "foodItemsList")
    private List<Restaurant> restaurants;

    @ManyToOne
    private FoodCategory foodCategory;

    @ManyToOne
    private FoodSubCategory foodSubCategory;

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
