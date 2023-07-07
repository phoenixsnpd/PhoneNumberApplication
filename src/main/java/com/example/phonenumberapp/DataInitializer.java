package com.example.phonenumberapp;

import com.example.phonenumberapp.entity.User;
import com.example.phonenumberapp.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {
    private final UserRepository repository;

    public DataInitializer(UserRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void init() {
        User user1 = new User("user1", "password1");
        User user2 = new User("user2", "password2");
        repository.save(user1);
        repository.save(user2);
    }
}
