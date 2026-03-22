package com.jdcolorado.gestions.eventos.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpeakerResponseDto {
    private Long id;
    private String name;
    private String email;
    private String bio;
}
