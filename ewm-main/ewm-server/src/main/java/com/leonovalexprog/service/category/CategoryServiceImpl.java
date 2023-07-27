package com.leonovalexprog.service.category;

import com.leonovalexprog.dto.category.CategoryDto;
import com.leonovalexprog.dto.category.NewCategoryDto;
import com.leonovalexprog.exception.exceptions.ConditionsViolationException;
import com.leonovalexprog.exception.exceptions.EntityNotExistsException;
import com.leonovalexprog.exception.exceptions.FieldValueExistsException;
import com.leonovalexprog.mapper.CategoryMapper;
import com.leonovalexprog.model.Category;
import com.leonovalexprog.repository.CategoriesRepository;
import com.leonovalexprog.repository.EventsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoriesRepository categoriesRepository;
    private final EventsRepository eventsRepository;

    @Override
    @Transactional
    public CategoryDto newCategory(NewCategoryDto newCategoryDto) {
        Category category = Category.builder()
                .name(newCategoryDto.getName())
                .build();

        try {
            Category newCategory = categoriesRepository.saveAndFlush(category);
            return CategoryMapper.toDto(newCategory);
        } catch (DataIntegrityViolationException exception) {
            throw new FieldValueExistsException(exception.getMessage());
        }
    }

    @Override
    @Transactional
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
    @Transactional
    public CategoryDto updateCategory(long categoryId, NewCategoryDto newCategoryDto) {
        Category category = categoriesRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotExistsException("Category with id=%d was not found"));
        category.setName(newCategoryDto.getName());

        try {
            Category newCategory = categoriesRepository.saveAndFlush(category);
            return CategoryMapper.toDto(newCategory);
        } catch (DataIntegrityViolationException exception) {
            throw new FieldValueExistsException(exception.getMessage());
        }
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        if (from < 0 || size < 0) {
            throw new IllegalArgumentException("From and Size must be positive or zero");
        }

        List<Category> categories = categoriesRepository.findAll(PageRequest.of(from / size, size)).getContent();

        return CategoryMapper.toDto(categories);
    }

    @Override
    public CategoryDto getCategory(long catId) {
        Category category = categoriesRepository.findById(catId)
                .orElseThrow(() -> new EntityNotExistsException(String.format("Category with id=%d was not found", catId)));

        return CategoryMapper.toDto(category);
    }
}
