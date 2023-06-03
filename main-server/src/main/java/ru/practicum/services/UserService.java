package ru.practicum.services;

import ru.practicum.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(UserDto userDto);

    void delete(long id);

    List<UserDto> find(Long[] ids, Integer from, Integer size);
}