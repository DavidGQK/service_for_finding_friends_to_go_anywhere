package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.UserDto;
import ru.practicum.dto.events.*;
import ru.practicum.models.Event;
import ru.practicum.repositories.events.CombineEventFilters;
import ru.practicum.services.CategoryService;
import ru.practicum.services.EventService;
import ru.practicum.services.UserService;
import ru.practicum.states.EventSortBy;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@SpringBootTest(classes = MainServerApp.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(locations = "classpath:application.properties")
@Transactional
public class EventsTests {
    private final UserService userService;
    private final EventService eventService;
    private final CategoryService categoryService;
    private final EntityManager em;

    @Test
    public void shouldCreateAndGetEvent() {
        CategoryDto categoryDto = new CategoryDto(null, "Категория");
        categoryDto = categoryService.create(categoryDto);
        UserDto userDto = new UserDto(null, "Иннокентий", "kesha@ya.ru");
        userDto = userService.create(userDto);
        CreateEventDto newEventDto = CreateEventDto.builder()
                .title("Заголовок")
                .annotation("Аннотация")
                .description("Описание")
                .eventDate("2022-10-11 10:09:08")
                .location(new Location(0, 0))
                .category(categoryDto.getId())
                .build();

        EventFullDto eventFullDto = eventService.create(userDto.getId(), newEventDto);
        TypedQuery<Event> query = em.createQuery("Select e from Event e where e.annotation = :annotation",
                Event.class);

        Event event = query.setParameter("annotation", newEventDto.getAnnotation()).getSingleResult();
        Assertions.assertNotNull(event);
        Assertions.assertEquals(event.getId(), eventFullDto.getId());
    }

    @Test
    public void shouldUpdateEvent() {
        CategoryDto catDto = new CategoryDto(null, "Категория");
        catDto = categoryService.create(catDto);
        UserDto userDto = new UserDto(null, "Дарья", "dasha@ya.ru");
        userDto = userService.create(userDto);
        CreateEventDto eventDto = CreateEventDto.builder()
                .title("Заголовок")
                .annotation("Аннотация")
                .description("Описание")
                .eventDate("2022-10-25 12:00:00")
                .location(new Location(0, 0))
                .category(catDto.getId())
                .build();

        EventFullDto fullEvent = eventService.create(userDto.getId(), eventDto);
        UpdateEventRequest request = new UpdateEventRequest();
        request.setEventId(fullEvent.getId());
        request.setAnnotation("Новая аннотация");
        eventService.update(userDto.getId(), request);
        TypedQuery<Event> eventQuery = em.createQuery("Select e from Event e where e.id = :id", Event.class);
        Event event = eventQuery.setParameter("id", fullEvent.getId()).getSingleResult();
        Assertions.assertNotNull(event);
        Assertions.assertEquals(event.getAnnotation(), request.getAnnotation());
    }

    @Test
    public void shouldFindEvent() {
        CategoryDto catDto = new CategoryDto(null, "Категория");
        catDto = categoryService.create(catDto);
        UserDto userDto = new UserDto(null, "Михаил", "misha@ya.ru");
        userDto = userService.create(userDto);
        CreateEventDto eventDto = CreateEventDto.builder()
                .title("Интригующий заголовок")
                .annotation("Встреча выпускников Яндекса")
                .description("Будет весело и интересно")
                .eventDate("2022-10-30 17:00:00")
                .location(new Location(0, 0))
                .category(catDto.getId())
                .build();

        EventFullDto fullDto = eventService.create(userDto.getId(), eventDto);
        CombineEventFilters combinedFilters = CombineEventFilters.builder().text("ЯНДЕКС").build();
        List<EventShortDto> list = eventService.findShortEvents(combinedFilters, EventSortBy.EVENT_DATE, 0, 10);
        Assertions.assertEquals(1, list.size());
    }
}