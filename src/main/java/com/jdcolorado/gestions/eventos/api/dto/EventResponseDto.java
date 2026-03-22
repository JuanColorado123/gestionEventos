package com.jdcolorado.gestions.eventos.api.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@JsonPropertyOrder({"id", "name", "date", "location"})
public class EventResponseDto {
    private Long id;
    private String name;
    private LocalDate date;
    private String location;

    private Long categoryId;
    private String categoryName;

    private Set<SpeakerResponseDto> speakers;

}
