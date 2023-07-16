package com.leonovalexprog.service.CategoriesService;

import com.leonovalexprog.CategoryDto;
import com.leonovalexprog.NewCategoryDto;

public interface CategoriesService {
    CategoryDto newCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(long categoryId);

    CategoryDto updateCategory(long categoryId, NewCategoryDto newCategoryDto);
}
