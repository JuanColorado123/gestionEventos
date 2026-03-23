package com.jdcolorado.gestions.eventos.api.dto;

import com.jdcolorado.gestions.eventos.api.domain.Speaker;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Schema(description = "Detalle de la solicitud para crear/acutalizar un evento")
public class EventRequestDto {
    @NotBlank(message = "el campo no puede estar vacio")

    private String name;

    @FutureOrPresent(message = "El campo debe ser una fecha futura o presente")
    @NotNull(message = "El campo no puede estar vacio")
    private LocalDate date;

    @NotBlank(message = "El campo no puede estar vacio")
    private String location;

    @NotNull(message = "El campo categoryId no puede estar vacio")
    private Long categoryId;

    private Set<Long> speakersIds = new HashSet<>();
}
