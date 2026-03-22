package com.jdcolorado.gestions.eventos.api.mapper;

import com.jdcolorado.gestions.eventos.api.domain.Role;
import com.jdcolorado.gestions.eventos.api.domain.User;
import com.jdcolorado.gestions.eventos.api.dto.UserResponseDto;
import com.jdcolorado.gestions.eventos.api.exception.InternalErrorException;
import com.jdcolorado.gestions.eventos.api.exception.ResourceNotFoundException;
import com.jdcolorado.gestions.eventos.api.repository.RoleRepository;
import com.jdcolorado.gestions.eventos.api.security.dto.RegisterDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    protected RoleRepository roleRepository;

    @Mapping(target = "password", ignore = true )
    @Mapping(target = "id", ignore = true )
    @Mapping(target = "roles", source = "registerDto.roles", qualifiedByName = "mapRoleStringToRole")
    @Mapping(target = "attendedEvents",ignore = true)
    public abstract User registerDtoToUser(RegisterDto registerDto);

    public abstract UserResponseDto toDto(User user);
    public abstract List<UserResponseDto> toDtoList(List<User> user);


    @Named("mapRoleStringToRole")
    public Set<Role> mapRoleStringToRole(Set<String> roleNames){

        if(roleNames == null || roleNames.isEmpty()){
         return roleRepository.findByName("ROLE_USER")
                 .map(Collections::singleton)
                 .orElseThrow(() -> new InternalErrorException("Error: Rol 'ROLE_USER', hubo una execption no esperada intente mas tarde")
                 );
        }

        return roleNames
                .stream()
                .map(
                        roleName -> roleRepository
                                .findByName(roleName)
                                .orElseThrow(() -> new ResourceNotFoundException("Error: Rol no existe:" + roleName))
                ).collect(Collectors.toSet());
    }
}
