package com.personal_finance.controller;

import com.personal_finance.dto.response.UserResponseDto;
import com.personal_finance.dto.resquest.UserRequestDto;
import com.personal_finance.entity.Users;
import com.personal_finance.mapper.UserMapper;
import com.personal_finance.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@RequestBody UserRequestDto userRequestDto){
        Users userCreated = usersService.save(userMapper.toEntity(userRequestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDto(userCreated));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable UUID id){
        Users user = usersService.searchById(id);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> updatePassword(@PathVariable UUID id, @RequestBody Users user){
        Users userToUpdatePassword = usersService.editPassword(id, user.getPassword());
        return ResponseEntity.ok(userMapper.toDto(userToUpdatePassword));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAll(){
        List<Users> users = usersService.findAll();

        List<UserResponseDto> listResponseDto = users.stream()
                .map(userMapper::toDto)
                .toList();

        return ResponseEntity.ok(listResponseDto);
    }
}