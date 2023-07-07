package com.example.phonenumberapp.service;

import com.example.phonenumberapp.entity.User;
import com.example.phonenumberapp.repository.UserRepository;
import com.example.phonenumberapp.security.AppUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository repository;

    public UserDetailsServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = repository.findByName(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("No user with such name");
        }

        return new  AppUserDetails(user.get());
    }
}
