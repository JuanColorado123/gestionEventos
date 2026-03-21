package com.jdcolorado.gestions.eventos.api.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonPropertyOrder({"id", "name", "date", "location"})
public class EventResponseDto {
    private Long id;
    private String name;
    private LocalDate date;
    private String location;
}
