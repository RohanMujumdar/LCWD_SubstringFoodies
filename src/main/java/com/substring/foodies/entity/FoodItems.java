package com.substring.foodies.entity;
import com.substring.foodies.dto.enums.FoodType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class FoodItems extends BaseAuditableEntity{

    @Id
    private String id;

    private String name;
    private String description;
    private int price;
    private Boolean isAvailable;

    @Enumerated(EnumType.STRING)
    private FoodType foodType = FoodType.VEG;

    @DecimalMin(value = "0.0")
    @DecimalMax(value = "5.0")
    @Column(name = "rating_star")
    private Double rating;

    private String imageUrl;

    private int discountAmount;

    @ManyToMany(mappedBy = "foodItemsList")
    private Set<Restaurant> restaurants = new HashSet<>();

    @ManyToOne
    private FoodCategory foodCategory;

    @ManyToOne
    private FoodSubCategory foodSubCategory;

    public int actualPrice()
    {
        return price - discountAmount;
    }

    public int getDiscountPercentage()
    {
        return (discountAmount / price) * 100;
    }
}
