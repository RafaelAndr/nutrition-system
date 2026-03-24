package com.nutrition_system.service;

import com.nutrition_system.entity.Users;
import com.nutrition_system.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private CustomUserDetailsService service;

    @Test
    void shouldReturnUserDetailsWhenUserExists() {
        Users user = new Users();
        user.setUsername("rafael");
        user.setPassword("123");
        user.setRole("USER");

        when(usersRepository.findByUsername("rafael"))
                .thenReturn(Optional.of(user));

        UserDetails result = service.loadUserByUsername("rafael");

        assertEquals("rafael", result.getUsername());
        assertEquals("123", result.getPassword());
        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(usersRepository.findByUsername("rafael"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            service.loadUserByUsername("rafael");
        });
    }
}