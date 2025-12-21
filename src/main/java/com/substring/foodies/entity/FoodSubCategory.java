package com.substring.foodies.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "food_sub_category")
public class FoodSubCategory extends BaseAuditableEntity{

    @Id
    private String id;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private FoodCategory foodCategory;

    @OneToMany(
            mappedBy = "foodSubCategory",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<FoodItems> foodItemList = new ArrayList<>();
}
