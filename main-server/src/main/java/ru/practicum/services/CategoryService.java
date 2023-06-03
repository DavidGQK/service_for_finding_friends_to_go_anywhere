package ru.practicum.services;

import ru.practicum.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto create(CategoryDto categoryDto);

    CategoryDto update(CategoryDto categoryDto);

    void deleteById(long id);

    List<CategoryDto> get(Integer from, Integer size);

    CategoryDto findById(long catId);
}