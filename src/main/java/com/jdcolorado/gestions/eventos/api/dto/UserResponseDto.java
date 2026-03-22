package com.jdcolorado.gestions.eventos.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Long id;
    private String name;
    private String username;
    private String email;

    private List<RoleDto> roleDtos;
    private List<EventSummaryDto> eventSummaryDtos;
}
