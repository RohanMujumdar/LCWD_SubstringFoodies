package com.substring.foodies.entity;
import com.substring.foodies.dto.enums.FoodType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class FoodItems extends BaseAuditableEntity{

    @Id
    private String id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    @Min(0)
    private int price;

    private Boolean isAvailable;

    @Enumerated(EnumType.STRING)
    private FoodType foodType = FoodType.VEG;

    @DecimalMin(value = "0.0")
    @DecimalMax(value = "5.0")
    @Column(name = "rating_star")
    private Double rating = 0.0;

    private String imageUrl;

    @Min(0)
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

    public int getDiscountPercentage() {
        if (price == 0) return 0;
        return (int) ((discountAmount * 100.0) / price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FoodItems)) return false;
        FoodItems other = (FoodItems) o;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
