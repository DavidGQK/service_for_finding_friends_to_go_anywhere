package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Data
public class UserDto {
    private Long id;

    @NotBlank
    private String name;

    @Email
    private String email;
}