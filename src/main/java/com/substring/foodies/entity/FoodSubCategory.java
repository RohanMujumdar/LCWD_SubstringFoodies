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
@Table(name = "food_sub_category")
public class FoodSubCategory {

    @Id
    private String id;

    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    private FoodCategory foodCategory;

    @OneToMany(mappedBy = "foodSubCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FoodItems> foodItemList = new ArrayList<>();
}
