package com.dju.woojiman.global.auth;

import com.dju.woojiman.domain.user.repository.UserRepository;
import com.dju.woojiman.domain.user.model.Users;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));

        return new CustomUserDetail(user);
    }

    public UserDetails loadUserById(String id) throws UsernameNotFoundException {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));

        return new CustomUserDetail(user);
    }
}
