package com.leonovalexprog.controller.admin;

import com.leonovalexprog.CategoryDto;
import com.leonovalexprog.NewCategoryDto;
import com.leonovalexprog.client.StatsClient;
import com.leonovalexprog.service.CategoriesService.CategoriesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
@Slf4j
public class CategoriesController {
    private final CategoriesService categoriesService;

    @PostMapping
    public CategoryDto postCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Create new category (name = {})", newCategoryDto.getName());
        return categoriesService.newCategory(newCategoryDto);
    }

    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable long catId) {
        log.info("Delete category (id = {})", catId);
        categoriesService.deleteCategory(catId);
    }

    @PatchMapping("/{catId}")
    public CategoryDto patchCategory(@PathVariable long catId,
                              @Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Patch category with new name (name = {})", newCategoryDto.getName());
        return categoriesService.updateCategory(catId, newCategoryDto);
    }
}
