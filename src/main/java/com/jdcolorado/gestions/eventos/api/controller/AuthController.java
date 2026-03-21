package com.jdcolorado.gestions.eventos.api.controller;

import com.jdcolorado.gestions.eventos.api.domain.Role;
import com.jdcolorado.gestions.eventos.api.domain.User;
import com.jdcolorado.gestions.eventos.api.dto.ApiResponse;
import com.jdcolorado.gestions.eventos.api.dto.JwtAuthResponseDto;
import com.jdcolorado.gestions.eventos.api.dto.LoginDto;
import com.jdcolorado.gestions.eventos.api.dto.RegisterDto;
import com.jdcolorado.gestions.eventos.api.mapper.UserMapper;
import com.jdcolorado.gestions.eventos.api.repository.RoleRepository;
import com.jdcolorado.gestions.eventos.api.repository.UserRepository;
import com.jdcolorado.gestions.eventos.api.security.jwt.JwtGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtGenerator jwtGenerator;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponseDto> authenticateUser(@RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToke(authentication);

        return new ResponseEntity<>(new JwtAuthResponseDto(token), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody RegisterDto registerDto){
        if(Boolean.TRUE.equals(userRepository.existsByUsername(registerDto.getUsername())) ){
            return new ResponseEntity<>(new ApiResponse("username de usuario ya existe:" + registerDto.getUsername(), false),  HttpStatus.BAD_REQUEST);
        }

        if(Boolean.TRUE.equals(userRepository.existsByEmail(registerDto.getEmail())) ){
            return new ResponseEntity<>(new ApiResponse("email de usuario ya existe:" + registerDto.getEmail(),false), HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.registerDtoToUser(registerDto);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Role roles = roleRepository.findAByName("ROLE_USER").orElseThrow(() -> new RuntimeException("Error, el role no existe"));
        user.setRoles(Collections.singleton(roles));

        userRepository.save(user);

        return new ResponseEntity<>(
                new ApiResponse("Usuario registrado", true),
                HttpStatus.CREATED
        );    }
}
