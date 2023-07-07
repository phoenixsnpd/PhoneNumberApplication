package com.example.phonenumberapp.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    long id;
    @Column(name = "name", unique = true, nullable = false, length = 50)
    String name;

    @Column(name = "password", nullable = false, length = 50)
    String password;

    @Column(name = "contacts")
    @OneToMany
    List<Contact> contacts;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getUsername() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
