package com.substring.foodies.service;

import com.substring.foodies.dto.FoodCategoryDto;
import com.substring.foodies.entity.FoodCategory;
import com.substring.foodies.exception.BadRequestException;
import com.substring.foodies.exception.ResourceNotFound;
import com.substring.foodies.repository.FoodCategoryRepository;
import jakarta.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class FoodCategoryServiceImpl implements FoodCategoryService {

    @Autowired
    private FoodCategoryRepository foodCategoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public FoodCategoryDto create(FoodCategoryDto dto) throws BadRequestException {

        if (foodCategoryRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new BadRequestException(
                    "Food category already exists with name = " + dto.getName()
            );
        }

        FoodCategory category = modelMapper.map(dto, FoodCategory.class);
        return modelMapper.map(foodCategoryRepository.save(category), FoodCategoryDto.class);
    }


    @Override
    public FoodCategoryDto getById(String id) {
        FoodCategory category = foodCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Category not found"));
        return modelMapper.map(category, FoodCategoryDto.class);
    }

    @Override
    public List<FoodCategoryDto> getAll() {
        return foodCategoryRepository.findAll()
                .stream()
                .map(cat -> modelMapper.map(cat, FoodCategoryDto.class))
                .toList();
    }

    @Override
    public FoodCategoryDto update(String id, FoodCategoryDto dto) {

        FoodCategory category = foodCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Category not found with id = " + id));

        // ✅ Name uniqueness check ONLY if name is changing
        if (dto.getName() != null &&
                !category.getName().equalsIgnoreCase(dto.getName()) &&
                foodCategoryRepository.existsByNameIgnoreCase(dto.getName())) {

            throw new BadRequestException(
                    "Food category already exists with name = " + dto.getName()
            );
        }

        category.setName(dto.getName());
        category.setDescription(dto.getDescription());

        return modelMapper.map(foodCategoryRepository.save(category), FoodCategoryDto.class);
    }

    @Override
    public FoodCategoryDto patch(String id, FoodCategoryDto dto) {

        FoodCategory category = foodCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Category not found with id = " + id));

        if (dto.getName() != null &&
                !category.getName().equalsIgnoreCase(dto.getName()) &&
                foodCategoryRepository.existsByNameIgnoreCase(dto.getName())) {

            throw new BadRequestException(
                    "Food category already exists with name = " + dto.getName()
            );
        }

        if (dto.getName() != null) {
            category.setName(dto.getName());
        }

        if (dto.getDescription() != null) {
            category.setDescription(dto.getDescription());
        }

        return modelMapper.map(foodCategoryRepository.save(category), FoodCategoryDto.class);
    }


    @Override
    public void delete(String id) {
        FoodCategory category = foodCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Category not found"));
        foodCategoryRepository.delete(category);
    }
}
