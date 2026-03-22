package com.jdcolorado.gestions.eventos.api.mapper;

import com.jdcolorado.gestions.eventos.api.domain.Role;
import com.jdcolorado.gestions.eventos.api.dto.RoleDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDto toDto(Role role);
    Role toEntity(RoleDto roleDto);
    List<RoleDto> toDtoList(List<Role> roles);
}
