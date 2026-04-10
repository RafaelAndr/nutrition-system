package com.personal_finance.service;

import com.personal_finance.dto.user.LoginUserDto;
import com.personal_finance.dto.user.UserRequestDto;
import com.personal_finance.mapper.UserMapper;
import com.personal_finance.repository.UsersRepository;
import com.personal_finance.security.JwtToken;
import com.personal_finance.security.JwtService;
import com.personal_finance.security.JwtUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsersService usersService;

    public JwtToken login(LoginUserDto dto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.username(), dto.password())
        );

        JwtUserDetails user = (JwtUserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(user);

        return new JwtToken(token);
    }

    @Transactional
    public void register(UserRequestDto userRequestDto) {
        usersService.register(userRequestDto);
    }
}
