package com.example.phonenumberapp.repository;

import com.example.phonenumberapp.entity.Contact;
import com.example.phonenumberapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findAllByUser(User user);
}
