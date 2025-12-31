package com.substring.foodies.service;

import com.substring.foodies.dto.FoodSubCategoryDto;
import com.substring.foodies.entity.FoodCategory;
import com.substring.foodies.entity.FoodItems;
import com.substring.foodies.entity.FoodSubCategory;
import com.substring.foodies.exception.BadRequestException;
import com.substring.foodies.exception.ResourceNotFound;
import com.substring.foodies.repository.FoodCategoryRepository;
import com.substring.foodies.repository.FoodSubCategoryRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class FoodSubCategoryServiceImpl implements FoodSubCategoryService {

    @Autowired
    private FoodSubCategoryRepository foodSubCategoryRepository;

    @Autowired
    private FoodCategoryRepository foodCategoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    private FoodSubCategory findAndValidate(String id)
    {
        FoodSubCategory foodSubCategory =  foodSubCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Food Sub-category not found with id = " + id));

        return foodSubCategory;
    }

    @Override
    public FoodSubCategoryDto create(FoodSubCategoryDto dto) {

        if (foodSubCategoryRepository.existsById(dto.getId())) {
            throw new IllegalStateException(
                    "Food Sub Category already exists with id = " + dto.getId()
            );
        }

        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new BadRequestException("Sub-category name is required");
        }

        if (dto.getFoodCategoryId() == null) {
            throw new BadRequestException("Food category is required");
        }

        FoodCategory category = foodCategoryRepository
                .findById(dto.getFoodCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFound("Food category not found with id = " +
                                dto.getFoodCategoryId())
                );

        if (foodSubCategoryRepository.existsByNameIgnoreCaseAndFoodCategoryId(
                dto.getName(), category.getId())) {

            throw new BadRequestException(
                    "Sub-category already exists with name = " + dto.getName()
            );
        }

        FoodSubCategory subCategory = modelMapper.map(dto, FoodSubCategory.class);
        subCategory.setFoodCategory(category);

        return modelMapper.map(
                foodSubCategoryRepository.save(subCategory),
                FoodSubCategoryDto.class
        );
    }


    @Override
    public FoodSubCategoryDto getById(String id) {
        FoodSubCategory subCategory = findAndValidate(id);

        return modelMapper.map(subCategory, FoodSubCategoryDto.class);
    }

    @Override
    public List<FoodSubCategoryDto> getAll() {
        return foodSubCategoryRepository.findAll()
                .stream()
                .map(sc -> modelMapper.map(sc, FoodSubCategoryDto.class))
                .toList();
    }

    @Override
    public FoodSubCategoryDto update(String id, FoodSubCategoryDto dto) {

        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new BadRequestException("Sub-category name is required");
        }

        if (dto.getFoodCategoryId() == null) {
            throw new BadRequestException("Food category is required");
        }

        FoodSubCategory subCategory = findAndValidate(id);

        String newName = dto.getName();
        String categoryId = dto.getFoodCategoryId();

        // uniqueness check (exclude same entity)
        if (!subCategory.getName().equalsIgnoreCase(newName) &&
                foodSubCategoryRepository.existsByNameIgnoreCaseAndFoodCategoryId(
                        newName, categoryId)) {

            throw new BadRequestException(
                    "Sub-category already exists with name = " + newName
            );
        }

        FoodCategory category = foodCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFound("Category not found with id = " + categoryId));

        subCategory.setName(newName);
        subCategory.setFoodCategory(category);

        return modelMapper.map(
                foodSubCategoryRepository.save(subCategory),
                FoodSubCategoryDto.class
        );
    }



    @Override
    public FoodSubCategoryDto patch(String id, FoodSubCategoryDto dto) {

        FoodSubCategory subCategory = findAndValidate(id);

        // patch name
        if (dto.getName() != null &&
                !dto.getName().equalsIgnoreCase(subCategory.getName())) {

            if (foodSubCategoryRepository.existsByNameIgnoreCaseAndFoodCategoryId(
                    dto.getName(),
                    subCategory.getFoodCategory().getId())) {

                throw new BadRequestException(
                        "Sub-category already exists with name = " + dto.getName()
                );
            }

            subCategory.setName(dto.getName());
        }

        // patch category
        if (dto.getFoodCategoryId() != null) {

            FoodCategory category = foodCategoryRepository
                    .findById(dto.getFoodCategoryId())
                    .orElseThrow(() ->
                            new ResourceNotFound(
                                    "Category not found with id = " +
                                            dto.getFoodCategoryId())
                    );

            subCategory.setFoodCategory(category);
        }

        return modelMapper.map(
                foodSubCategoryRepository.save(subCategory),
                FoodSubCategoryDto.class
        );
    }


    @Override
    public void delete(String id) {
        FoodSubCategory subCategory = findAndValidate(id);

        foodSubCategoryRepository.delete(subCategory);
    }
}
