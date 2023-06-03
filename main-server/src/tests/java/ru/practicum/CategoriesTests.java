package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.CategoryDto;
import ru.practicum.models.Category;
import ru.practicum.services.CategoryService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(locations = "classpath:application.properties")
@Transactional
@SpringBootTest(classes = MainServerApp.class)
public class CategoriesTests {
    private final CategoryService categoryService;
    private final EntityManager em;

    @Test
    public void shouldCreateAndGetCategory() {
        CategoryDto dto = new CategoryDto(null, "Новая категория");
        dto = categoryService.create(dto);
        TypedQuery<Category> categoryQuery = em.createQuery("select c from Category c where c.name = :name",
                Category.class);

        Category category = categoryQuery.setParameter("name", dto.getName()).getSingleResult();
        Assertions.assertNotNull(category);
        Assertions.assertEquals(category.getId(), dto.getId());
    }
}