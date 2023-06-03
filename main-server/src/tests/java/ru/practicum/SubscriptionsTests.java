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
import ru.practicum.dto.SubscriptionDto;
import ru.practicum.dto.UserDto;
import ru.practicum.dto.events.CreateEventDto;
import ru.practicum.dto.events.EventFullDto;
import ru.practicum.dto.events.EventShortDto;
import ru.practicum.dto.events.Location;
import ru.practicum.models.Subscription;
import ru.practicum.services.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Transactional
@SpringBootTest(classes = MainServerApp.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(locations = "classpath:application.properties")
public class SubscriptionsTests {
    private final EventService eventService;
    private final RequestService requestService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final EntityManager em;
    private final SubscriptionService subscriptionService;

    @Test
    public void shouldCreateSubscription() {
        UserDto userDto = new UserDto(null, "Денис", "denis@ya.ru");
        userDto = userService.create(userDto);
        UserDto friendDto = new UserDto(null, "Гена", "gena@ya.ru");
        friendDto = userService.create(friendDto);
        SubscriptionDto subDto = subscriptionService.create(userDto.getId(), friendDto.getId());
        TypedQuery<Subscription> query = em.createQuery("Select s from Subscription s where s.user.id = :userId and " +
                "s.friend.id = :friendId", Subscription.class);

        Subscription subscription = query
                .setParameter("userId", userDto.getId())
                .setParameter("friendId", friendDto.getId())
                .getSingleResult();
        Assertions.assertEquals(subscription.getId(), subscription.getId());
    }

    @Test
    public void shouldGetSubscriptions() {
        UserDto userDto = new UserDto(null, "Денис", "denis@ya.ru");
        userDto = userService.create(userDto);
        UserDto friendDto = new UserDto(null, "Гена", "gena@ya.ru");
        friendDto = userService.create(friendDto);
        SubscriptionDto subDto = subscriptionService.create(userDto.getId(), friendDto.getId());
        List<SubscriptionDto> subscriptionsList = subscriptionService.getSubscriptions(userDto.getId());
        Assertions.assertEquals(1, subscriptionsList.size());
        Assertions.assertEquals(subDto.getId(), subscriptionsList.get(0).getId());
    }

    @Test
    public void shouldGetEvents() {
        CategoryDto catDto = new CategoryDto(null, "Категория");
        catDto = categoryService.create(catDto);
        UserDto eventCreator = new UserDto(null, "Марина", "marina@ya.ru");
        eventCreator = userService.create(eventCreator);
        CreateEventDto eventDto = CreateEventDto.builder()
                .title("Заголовок")
                .annotation("Аннотация")
                .description("Описание")
                .eventDate("2022-10-14 10:09:08")
                .location(new Location(0, 0))
                .category(catDto.getId())
                .build();

        EventFullDto fullEvent = eventService.create(eventCreator.getId(), eventDto);
        eventService.publish(fullEvent.getId());
        UserDto friend = new UserDto(0L, "Федор", "fedya@ya.ru");
        friend = userService.create(friend);
        RequestDto request = requestService.create(friend.getId(), fullEvent.getId());
        requestService.confirmRequest(eventCreator.getId(), fullEvent.getId(), request.getId());
        UserDto user = new UserDto(null, "Никита", "nikita@ya.ru");
        user = userService.create(user);
        SubscriptionDto subscription = subscriptionService.create(user.getId(), friend.getId());
        List<EventShortDto> eventsList = subscriptionService.getEvents(user.getId());
        Assertions.assertEquals(1, eventsList.size());
        Assertions.assertEquals(fullEvent.getId(), eventsList.get(0).getId());
    }

    @Test
    public void shouldDeleteSubscription() {
        UserDto user = new UserDto(null, "Федор", "fedya@ya.ru");
        user = userService.create(user);
        UserDto friend = new UserDto(null, "Никита", "nikita@ya.ru");
        friend = userService.create(friend);
        SubscriptionDto subscription = subscriptionService.create(user.getId(), friend.getId());

        subscriptionService.delete(subscription.getId());
        TypedQuery<Subscription> subQuery = em.createQuery("Select s from Subscription s where s.id = :id",
                Subscription.class);

        List<Subscription> subscriptionsList = subQuery
                .setParameter("id", subscription.getId())
                .getResultList();

        Assertions.assertEquals(0, subscriptionsList.size());
    }
}