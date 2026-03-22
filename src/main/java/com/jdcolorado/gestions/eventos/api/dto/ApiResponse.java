package com.jdcolorado.gestions.eventos.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private String message;
    private int httpStatus;
    private boolean success;
    private T body;
}