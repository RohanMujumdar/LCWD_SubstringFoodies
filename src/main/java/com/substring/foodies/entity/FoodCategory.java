package com.substring.foodies.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "food_category")
public class FoodCategory {

    @Id
    private String id;

    private String name;

    private String description;

    @OneToMany(mappedBy = "foodCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FoodItems> foodItemList = new ArrayList<>();

    @OneToMany(mappedBy = "foodCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FoodSubCategory> foodSubCategoryList = new ArrayList<>();

}
