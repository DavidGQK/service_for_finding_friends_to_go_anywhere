package ru.practicum.controllers.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CategoryDto;
import ru.practicum.services.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/admin/categories")
public class CategoryControllerAdmin {
    private final CategoryService categoryService;

    public CategoryControllerAdmin(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public CategoryDto create(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Category {} is created!", categoryDto);
        return categoryService.create(categoryDto);
    }

    @DeleteMapping(value = "/{catId}")
    public void deleteById(@PathVariable @Positive long catId) {
        categoryService.deleteById(catId);
        log.info("Category with id={} deleted!", catId);
    }

    @PatchMapping
    public CategoryDto update(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Category {} updated!", categoryDto);
        return categoryService.update(categoryDto);
    }
}