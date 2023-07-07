package com.example.phonenumberapp.service;

import com.example.phonenumberapp.entity.User;
import com.example.phonenumberapp.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public void saveUser(User user) {
        repository.save(user);
    }

    public void findUserByName(String name) {
        repository.findByName(name);
    }
}
