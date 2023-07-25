package com.leonovalexprog.controller.category;

import com.leonovalexprog.dto.CategoryDto;
import com.leonovalexprog.dto.NewCategoryDto;
import com.leonovalexprog.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
@Slf4j
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CategoryDto postCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Create new category (name = {})", newCategoryDto.getName());
        return categoryService.newCategory(newCategoryDto);
    }

    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable long catId) {
        log.info("Delete category (id = {})", catId);
        categoryService.deleteCategory(catId);
    }

    @PatchMapping("/{catId}")
    public CategoryDto patchCategory(@PathVariable long catId,
                              @Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Patch category with new name (name = {})", newCategoryDto.getName());
        return categoryService.updateCategory(catId, newCategoryDto);
    }
}
