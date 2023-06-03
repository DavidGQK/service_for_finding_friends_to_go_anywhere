package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class CreateCompilationDto {
    @NotBlank
    private String title;

    private Boolean pinned;

    @NotNull
    private Set<Long> events;
}