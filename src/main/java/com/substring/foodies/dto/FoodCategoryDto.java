package com.substring.foodies.dto;

import com.substring.foodies.entity.FoodSubCategory;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodCategoryDto {
    private String id;
    private String name;
    private String description;
    private List<FoodItemsDto> foodItemList = new ArrayList<>();
    private List<FoodSubCategory> foodSubCategoryList = new ArrayList<>();
}
