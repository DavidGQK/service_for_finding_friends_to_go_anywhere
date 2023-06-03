package ru.practicum.controllers.authorized;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.SubscriptionDto;
import ru.practicum.dto.events.EventShortDto;
import ru.practicum.services.SubscriptionService;

import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/subscription")
public class SubscriptionControllerAuthorized {
    private final SubscriptionService subscriptionService;

    @PostMapping(value = "/user/{userId}/friend/{friendId}")
    public SubscriptionDto create(@PathVariable @Positive  long userId,
                                  @PathVariable @Positive long friendId) {
        log.info("The user with id={} added the user with id={} as a friend", userId, friendId);
        return subscriptionService.create(userId, friendId);
    }

    @DeleteMapping(value = "/{subscriptionId}")
    public void delete(@PathVariable @Positive long subscriptionId) {
        log.info("Subscription with id={} deleted", subscriptionId);
        subscriptionService.delete(subscriptionId);
    }

    @GetMapping(value = "/user/{userId}/events")
    public List<EventShortDto> getEvents(@PathVariable @Positive long userId) {
        log.info("Getting the list of events from the user with id={}", userId);
        return subscriptionService.getEvents(userId);
    }

    @GetMapping(value = "/user/{userId}")
    public List<SubscriptionDto> getSubscriptions(@PathVariable @Positive long userId) {
        log.info("Getting subscriptions from a user with id={}", userId);
        return subscriptionService.getSubscriptions(userId);
    }
}