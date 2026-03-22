package com.jdcolorado.gestions.eventos.api.security.controller;

import com.jdcolorado.gestions.eventos.api.domain.User;
import com.jdcolorado.gestions.eventos.api.dto.ApiResponse;
import com.jdcolorado.gestions.eventos.api.security.dto.JwtAuthResponseDto;
import com.jdcolorado.gestions.eventos.api.security.dto.LoginDto;
import com.jdcolorado.gestions.eventos.api.security.dto.RegisterDto;
import com.jdcolorado.gestions.eventos.api.mapper.UserMapper;
import com.jdcolorado.gestions.eventos.api.repository.RoleRepository;
import com.jdcolorado.gestions.eventos.api.repository.UserRepository;
import com.jdcolorado.gestions.eventos.api.security.jwt.JwtGenerator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.print.attribute.standard.NumberUp;

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
    public ResponseEntity registerUser(@Valid @RequestBody RegisterDto registerDto){
        if(Boolean.TRUE.equals(userRepository.existsByUsername(registerDto.getUsername())) ){
            return new ResponseEntity<>(new ApiResponse(
                    "username de usuario ya existe:" + registerDto.getUsername(),
                    404,
                    false,
                    null),
                    HttpStatus.BAD_REQUEST);
        }

        if(Boolean.TRUE.equals(userRepository.existsByEmail(registerDto.getEmail())) ){
            return new ResponseEntity<>(new ApiResponse(
                    "email de usuario ya existe:" + registerDto.getEmail(),
                    404 ,
                    false,
                    null),
                    HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.registerDtoToUser(registerDto);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        userRepository.save(user);

        return new ResponseEntity<>(
                new ApiResponse(
                        "Usuario registrado",
                        201,
                        true,
                        "Usuario guardado correctamente"),
                HttpStatus.CREATED
        );
    }
}
