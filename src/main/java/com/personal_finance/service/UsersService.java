package com.personal_finance.service;

import com.personal_finance.dto.user.ChangePasswordDto;
import com.personal_finance.dto.user.UserRequestDto;
import com.personal_finance.dto.user.UserResponseDto;
import com.personal_finance.entity.Users;
import com.personal_finance.entity.enums.Role;
import com.personal_finance.exception.AccessForbiddenException;
import com.personal_finance.exception.EntityAlreadyExistsException;
import com.personal_finance.mapper.UserMapper;
import com.personal_finance.repository.UsersRepository;
import com.personal_finance.security.SecurityService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final SecurityService securityService;

    public void register(UserRequestDto userRequestDto) {

        log.info("Attempting to register user with username '{}'", userRequestDto.username());

        if (usersRepository.existsByUsername(userRequestDto.username())) {
            log.warn("Registration failed: username '{}' already exists", userRequestDto.username());
            throw new EntityAlreadyExistsException("User already exists");
        }

        if (!userRequestDto.password().equals(userRequestDto.confirmPassword())) {
            log.warn("Registration failed for username '{}': passwords do not match", userRequestDto.username());
            throw new AccessForbiddenException("Passwords do not match");
        }

        Users user = new Users();
        user.setName(userRequestDto.name());
        user.setUsername(userRequestDto.username());
        user.setPassword(passwordEncoder.encode(userRequestDto.password()));
        user.setRole(Role.ROLE_CLIENT);

        Users savedUser = usersRepository.save(user);

        log.info("User '{}' registered successfully with id {}", savedUser.getUsername(), savedUser.getId());
    }

    @Transactional(readOnly = true)
    public Users searchById(UUID id) {
        return usersRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );
    }

    @Transactional(readOnly = true)
    public UserResponseDto getMe(){
        Users userLogged = securityService.getUserLoggedIn();
        return userMapper.toDto(searchById(userLogged.getId()));
    }

    public void changePassword(ChangePasswordDto dto) {
        Users user = securityService.getUserLoggedIn();

        log.info("User {} is attempting to change password", user.getId());

        if (!passwordEncoder.matches(dto.currentPassword(), user.getPassword())) {
            log.warn("User {} provided incorrect current password", user.getId());
            throw new AccessForbiddenException("Current password is incorrect");
        }

        if (passwordEncoder.matches(dto.newPassword(), user.getPassword())) {
            log.warn("User {} attempted to reuse the same password", user.getId());
            throw new AccessForbiddenException("New password cannot be the same as the current password");
        }

        user.setPassword(passwordEncoder.encode(dto.newPassword()));

        usersRepository.save(user);

        log.info("User {} changed password successfully", user.getId());
    }

    @Transactional(readOnly = true)
    public List<Users> findAll() {
        return usersRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Users searchByUsername(String username) {
        return usersRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException(String.format("Username '%s' not found", username))
        );
    }

    public void deleteUser(UUID id) {
        Users user = searchById(id);
        usersRepository.delete(user);
    }

    public void promoteToAdmin(UUID userId) {
        Users user = searchById(userId);

        user.setRole(Role.ROLE_ADMIN);
        usersRepository.save(user);
    }
}
