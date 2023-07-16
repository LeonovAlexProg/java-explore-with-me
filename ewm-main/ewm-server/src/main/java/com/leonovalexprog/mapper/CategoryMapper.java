package com.leonovalexprog.mapper;

import com.leonovalexprog.CategoryDto;
import com.leonovalexprog.model.Category;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CategoryMapper {
    public static CategoryDto toDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
