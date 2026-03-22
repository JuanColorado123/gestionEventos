package com.jdcolorado.gestions.eventos.api.security.dto;

import com.jdcolorado.gestions.eventos.api.domain.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {

    @NotBlank(message = "El campo username, no puede estar vacio")
    @Size(min = 4, max = 15, message = "El campo username, debe estar entre 4 y 15 caracteres")
    private String username;

    @NotBlank(message = "El campo password, no puede estar vacio")
    @Size(min = 6, max = 100, message = "El campo password, debe estar entre 4 y 100 caracteres")
    private String password;

    @NotBlank(message = "El campo email, no puede estar vacio")
    @Email(message = "El campo email, no cumple con el formato")
    private String email;

    @NotBlank(message = "El campo name, no puede estar vacio")
    private String name;

    private Set<String> roles;
}
