package com.jdcolorado.gestions.eventos.api.service;

import com.jdcolorado.gestions.eventos.api.domain.Role;

import java.util.List;
import java.util.Optional;

public interface IRoleService{
    Role findById(Long id);
    List<Role> findAll();
    Role save(Role role);
    void delete(Long id);
}
