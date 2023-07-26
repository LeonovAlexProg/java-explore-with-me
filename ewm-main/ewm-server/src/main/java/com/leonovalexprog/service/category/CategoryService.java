package com.leonovalexprog.service.category;

import com.leonovalexprog.dto.category.CategoryDto;
import com.leonovalexprog.dto.category.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto newCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(long categoryId);

    CategoryDto updateCategory(long categoryId, NewCategoryDto newCategoryDto);

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategory(long catId);
}
