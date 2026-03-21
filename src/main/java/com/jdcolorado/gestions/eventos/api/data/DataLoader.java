package com.jdcolorado.gestions.eventos.api.data;

import com.jdcolorado.gestions.eventos.api.domain.Role;
import com.jdcolorado.gestions.eventos.api.domain.User;
import com.jdcolorado.gestions.eventos.api.repository.RoleRepository;
import com.jdcolorado.gestions.eventos.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public void run(String... args) throws Exception {

        Role adminRole = roleRepository.findAByName("ROLE_ADMIN").orElseGet(() -> {
            Role newRole = new Role();
                newRole.setName("ROLE_ADMIN");
                return roleRepository.save(newRole);
        });

        Role userRole = roleRepository.findAByName("ROLE_USER").orElseGet(() -> {
            Role newRole = new Role();
            newRole.setName("ROLE_USER");
            return roleRepository.save(newRole);
        });

        if(userRepository.findByUsername("admin").isEmpty()) {
            User newUser = new User();
            newUser.setName("Juan David Colorado Montenegro");
            newUser.setEmail("juandavid@example.com");
            newUser.setUsername("admin");
            newUser.setPassword(passwordEncoder.encode("12345"));

            Set<Role> roles = new HashSet<>();

            roles.add(adminRole);
            roles.add(userRole);
            newUser.setRoles(roles);
            userRepository.save(newUser);
        }

        if(userRepository.findByUsername("jpablito").isEmpty()) {
            User newUser = new User();
            newUser.setName("Juan Pablo Monsalve Galvan");
            newUser.setEmail("juanpablo@example.com");
            newUser.setUsername("jpablito");
            newUser.setPassword(passwordEncoder.encode("12345"));

            Set<Role> roles = new HashSet<>();
            newUser.setRoles(roles);
            roles.add(adminRole);
            roles.add(userRole);

            userRepository.save(newUser);
        }
    }
}
