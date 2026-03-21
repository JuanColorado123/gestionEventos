package com.jdcolorado.gestions.eventos.api.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EventRequestDto {
    @NotBlank(message = "el campo no puede estar vacio")
    private String name;

    @FutureOrPresent(message = "El campo debe ser una fecha futura o presente")
    @NotNull(message = "El campo no puede estar vacio")
    private LocalDate date;

    @NotBlank(message = "El campo no puede estar vacio")
    private String location;
}
