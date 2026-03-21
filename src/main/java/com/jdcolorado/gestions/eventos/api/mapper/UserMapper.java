package com.jdcolorado.gestions.eventos.api.mapper;

import com.jdcolorado.gestions.eventos.api.domain.User;
import com.jdcolorado.gestions.eventos.api.dto.RegisterDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "password", ignore = true )
    @Mapping(target = "id", ignore = true )
    User registerDtoToUser(RegisterDto registerDto);

}
