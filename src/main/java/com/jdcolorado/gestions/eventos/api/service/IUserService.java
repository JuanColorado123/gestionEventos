package com.jdcolorado.gestions.eventos.api.service;

import com.jdcolorado.gestions.eventos.api.domain.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    User findById(Long id);
    Optional<User> findByUsername(String username);
    List<User> findAll();
    User save(User user);
    void delete(Long id);
}
