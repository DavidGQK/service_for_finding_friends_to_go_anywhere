package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.CreateCompilationDto;
import ru.practicum.dto.UserDto;
import ru.practicum.dto.events.CreateEventDto;
import ru.practicum.dto.events.EventFullDto;
import ru.practicum.dto.events.Location;
import ru.practicum.models.Compilation;
import ru.practicum.services.CategoryService;
import ru.practicum.services.CompilationService;
import ru.practicum.services.EventService;
import ru.practicum.services.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Set;

@SpringBootTest(classes = MainServerApp.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(locations = "classpath:application.properties")
@Transactional
public class CompilationsTests {
    private final UserService userService;
    private final CategoryService categoryService;
    private final CompilationService compilationService;
    private final EntityManager em;
    private final EventService eventService;

    @Test
    public void shouldCreateAndGetCompilation() {
        CategoryDto catDto = new CategoryDto(null, "Новая категория");
        catDto = categoryService.create(catDto);
        UserDto userDto = new UserDto(null, "Петр", "petr@yandex.ru");
        userDto = userService.create(userDto);
        CreateEventDto eventDto = CreateEventDto.builder()
                .title("Заголовок")
                .annotation("Короткое описание")
                .description("Описание")
                .eventDate("2022-10-15 11:10:09")
                .location(new Location(0, 0))
                .category(catDto.getId())
                .build();

        EventFullDto fullEventDto = eventService.create(userDto.getId(), eventDto);
        eventService.publish(fullEventDto.getId());
        CreateEventDto eventDto1 = CreateEventDto.builder()
                .title("Новый заголовок")
                .annotation("Новое короткое описание")
                .description("Новое описание")
                .eventDate("2022-10-14 10:09:08")
                .location(new Location(0, 0))
                .category(catDto.getId())
                .build();

        EventFullDto fullEventDto1 = eventService.create(userDto.getId(), eventDto1);
        eventService.publish(fullEventDto1.getId());
        CreateCompilationDto newCompilationDto = CreateCompilationDto.builder()
                .events(Set.of(fullEventDto.getId(), fullEventDto1.getId()))
                .title("Заголовок")
                .pinned(true)
                .build();

        CompilationDto compDto = compilationService.create(newCompilationDto);
        TypedQuery<Compilation> compQuery = em.createQuery("Select c from Compilation c where c.title = :title",
                Compilation.class);

        Compilation compilation = compQuery
                .setParameter("title", compDto.getTitle())
                .getSingleResult();

        Assertions.assertNotNull(compilation);
        Assertions.assertEquals(compilation.getId(), compDto.getId());
    }
}