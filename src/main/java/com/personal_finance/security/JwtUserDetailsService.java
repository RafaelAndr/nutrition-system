package com.personal_finance.security;

import com.personal_finance.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UsersService usersService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return new JwtUserDetails(usersService.searchByUsername(username));
    }
}
