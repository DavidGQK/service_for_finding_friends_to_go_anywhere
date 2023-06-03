package ru.practicum.controllers.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.UserDto;
import ru.practicum.services.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/admin/users")
public class UserControllerAdmin {
    private final UserService userService;

    public UserControllerAdmin(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        log.info("User {} has been created!", userDto);
        return userService.create(userDto);
    }

    @DeleteMapping(value = "/{userId}")
    public void delete(@PathVariable @Positive long userId) {
        userService.delete(userId);
        log.info("User with id={} deleted!", userId);
    }

    @GetMapping
    public List<UserDto> find(@RequestParam(required = false) Long[] ids,
                              @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                              @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        log.info("The following users were found by the query:");
        return userService.find(ids, from, size);
    }
}