package com.example.phonenumberapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "contacts")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class Contact {
    @Id
    @Column(name = "contact_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    long id;
    @Column(name ="name", unique = true, nullable = false)
    String name;
    @Column(name = "phone_numbers")
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "contact_id")
    List<ContactPhoneNumber> contactPhoneNumbers;
    @Column(name = "emails")
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "contact_id")
    List<ContactEmail> emails;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    User user;

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", phoneNumbers=" + contactPhoneNumbers +
                ", emails=" + emails +
                '}';
    }
}
