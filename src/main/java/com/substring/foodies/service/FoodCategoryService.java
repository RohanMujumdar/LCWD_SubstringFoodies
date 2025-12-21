package com.substring.foodies.service;

import com.substring.foodies.dto.FoodCategoryDto;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface FoodCategoryService {

    FoodCategoryDto create(FoodCategoryDto dto) throws BadRequestException;

    FoodCategoryDto getById(String id);

    List<FoodCategoryDto> getAll();

    FoodCategoryDto update(String id, FoodCategoryDto dto);

    FoodCategoryDto patch(String id, FoodCategoryDto dto);

    void delete(String id);
}
