package ru.practicum.mappers;

import ru.practicum.dto.SubscriptionDto;
import ru.practicum.models.Subscription;

public class SubscriptionMapper {
    public static SubscriptionDto toSubscriptionDto(Subscription subscription) {
        return SubscriptionDto.builder()
                .id(subscription.getId())
                .user(UserMapper.toUserShortDto(subscription.getUser()))
                .friend(UserMapper.toUserShortDto(subscription.getFriend()))
                .build();
    }
}