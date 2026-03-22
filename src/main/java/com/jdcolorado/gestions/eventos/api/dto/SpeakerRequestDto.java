package com.jdcolorado.gestions.eventos.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class SpeakerRequestDto {

    @NotBlank(message = "El campo 'name' no puede estar vacío")
    @Size(max = 80, message = "El campo 'name' debe tener como máximo 80 caracteres")
    private String name;

    @NotEmpty(message = "El campo 'email' no puede estar vacío")
    @Email(message = "El campo 'email' no tiene un formato válido")
    private String email;

    @NotEmpty(message = "El campo 'bio' no puede estar vacío")
    @Size(max = 500, message = "El campo 'bio' puede tener como máximo 500 caracteres")
    private String bio;
}
