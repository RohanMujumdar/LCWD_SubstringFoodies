package com.substring.foodies.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "food_category")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FoodCategory extends BaseAuditableEntity {

    @Id
    private String id;

    private String name;

    private String description;

    // 👇 controls category order in menu
    private int displayOrder;

    @OneToMany(mappedBy = "foodCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FoodSubCategory> foodSubCategoryList = new ArrayList<>();
}
