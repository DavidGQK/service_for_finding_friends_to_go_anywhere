package ru.practicum.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.CategoryDto;
import ru.practicum.errors.exceptions.NotFoundException;
import ru.practicum.mappers.CategoryMapper;
import ru.practicum.models.Category;
import ru.practicum.repositories.CategoryRepository;
import ru.practicum.services.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryDto create(CategoryDto dto) {
        if ((dto.getId() != null) && (dto.getId() != 0)) {
            throw new IllegalArgumentException("The original category id must be 0");
        }
        Category category = CategoryMapper.toCategory(dto);
        category = categoryRepository.save(category);
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    @Transactional
    public CategoryDto update(CategoryDto dto) {
        if ((dto.getId() == null) || (dto.getId() == 0)) {
            throw new IllegalArgumentException("The category id must be specified");
        }
        Category category = CategoryMapper.toCategory(dto);
        category = categoryRepository.save(category);
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("There is no category with this id!"));
        categoryRepository.deleteById(id);
    }

    @Override
    public List<CategoryDto> get(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return categoryRepository.findAll(pageable).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto findById(long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("There is no category with this id!"));
        return CategoryMapper.toCategoryDto(category);
    }
}