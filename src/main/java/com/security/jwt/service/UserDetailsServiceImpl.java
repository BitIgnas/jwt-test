package com.security.jwt.service;

import com.security.jwt.model.User;
import com.security.jwt.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

import static java.util.Collections.singletonList;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "found with username : " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), user.isEnabled(),
                true, true, true,
                getAuthorities());
    }

    private Collection<? extends GrantedAuthority> getAuthorities() {
        return singletonList(new SimpleGrantedAuthority("USER"));

    }
}
