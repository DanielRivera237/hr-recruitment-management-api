package com.uca.rrhhbackend.config;

import com.uca.rrhhbackend.entity.Role;
import com.uca.rrhhbackend.entity.User;
import com.uca.rrhhbackend.repository.RoleRepository;
import com.uca.rrhhbackend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeAdmin(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            Role adminRole = roleRepository
                    .findByNameIgnoreCase("ADMIN")
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName("ADMIN");
                        role.setActive(true);

                        return roleRepository.save(role);
                    });

            String adminEmail = "admin@rrhh.com";

            if (!userRepository.existsByEmailIgnoreCase(adminEmail)) {

                User admin = new User();
                admin.setName("Administrador");
                admin.setSurname("Sistema");
                admin.setEmail(adminEmail);
                admin.setPassword(
                        passwordEncoder.encode("Admin123")
                );
                admin.setActive(true);
                admin.setBlocked(false);
                admin.setRole(adminRole);

                userRepository.save(admin);

                System.out.println(
                        "Usuario administrador creado: " + adminEmail
                );
            }
        };
    }
}