package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.RequestDto;
import ru.practicum.dto.UserDto;
import ru.practicum.dto.events.CreateEventDto;
import ru.practicum.dto.events.EventFullDto;
import ru.practicum.dto.events.Location;
import ru.practicum.models.Request;
import ru.practicum.services.CategoryService;
import ru.practicum.services.EventService;
import ru.practicum.services.RequestService;
import ru.practicum.services.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

@Transactional
@SpringBootTest(classes = MainServerApp.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(locations = "classpath:application.properties")
public class RequestsTests {
    private final EventService eventService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final EntityManager em;
    private final RequestService requestService;

    @Test
    public void shouldCreateAndGetRequest() {
        CategoryDto dto = new CategoryDto(null, "Выставки");
        dto = categoryService.create(dto);
        UserDto userDto = new UserDto(null, "Мария", "masha@yandex.ru");
        userDto = userService.create(userDto);
        UserDto requester = new UserDto(null, "Павел", "pasha@yandex.ru");
        requester = userService.create(requester);

        CreateEventDto createEventDto = CreateEventDto.builder()
                .title("Заголовок")
                .annotation("Аннотация")
                .description("Описание")
                .eventDate("2023-01-01 17:00:00")
                .location(new Location(0, 0))
                .category(dto.getId())
                .build();

        EventFullDto eventFullDto = eventService.create(userDto.getId(), createEventDto);
        eventService.publish(eventFullDto.getId());
        RequestDto requestDto = requestService.create(requester.getId(), eventFullDto.getId());
        TypedQuery<Request> requestQuery = em.createQuery("Select r from Request r where r.eventId = :eventId and r" +
                ".userId = :userId", Request.class);

        Request request = requestQuery
                .setParameter("eventId", eventFullDto.getId())
                .setParameter("userId", requester.getId())
                .getSingleResult();

        Assertions.assertNotNull(request);
        Assertions.assertEquals(request.getId(), requestDto.getId());
    }
}