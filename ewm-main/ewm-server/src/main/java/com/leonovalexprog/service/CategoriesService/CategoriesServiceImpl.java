package com.leonovalexprog.service.CategoriesService;

import com.leonovalexprog.CategoryDto;
import com.leonovalexprog.NewCategoryDto;
import com.leonovalexprog.exception.exceptions.ConditionsViolationException;
import com.leonovalexprog.exception.exceptions.EntityNotExistsException;
import com.leonovalexprog.exception.exceptions.NameExistsException;
import com.leonovalexprog.mapper.CategoryMapper;
import com.leonovalexprog.model.Category;
import com.leonovalexprog.repository.CategoriesRepository;
import com.leonovalexprog.repository.EventsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoriesServiceImpl implements CategoriesService{
    private final CategoriesRepository categoriesRepository;
    private final EventsRepository eventsRepository;
    @Override
    public CategoryDto newCategory(NewCategoryDto newCategoryDto){
        Category category = Category.builder()
                .name(newCategoryDto.getName())
                .build();

        try {
            Category newCategory = categoriesRepository.saveAndFlush(category);
            return CategoryMapper.toDto(newCategory);
        } catch (DataIntegrityViolationException exception) {
            throw new NameExistsException(exception.getMessage());
        }
    }

    @Override
    public void deleteCategory(long categoryId) {
        if (!categoriesRepository.existsById(categoryId)) {
            throw new EntityNotExistsException(String.format("Category with id=%d was not found", categoryId));
        }
        if (eventsRepository.existsByCategory(categoryId)) {
            throw new ConditionsViolationException("The category is not empty");
        }

        categoriesRepository.deleteById(categoryId);
    }

    @Override
    public CategoryDto updateCategory(long categoryId, NewCategoryDto newCategoryDto) {
        Category category = categoriesRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotExistsException("Category with id=%d was not found"));
        category.setName(newCategoryDto.getName());

        try {
            Category newCategory = categoriesRepository.saveAndFlush(category);
            return CategoryMapper.toDto(newCategory);
        } catch (DataIntegrityViolationException exception) {
            throw new NameExistsException(exception.getMessage());
        }
    }
}
