package com.example.phonenumberapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "emails")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ContactEmail {
    @Id
    @Column(name = "email_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    long id;
    @Email
    @Column(name = "email")
    String email;
    @ManyToOne
    @JoinColumn(name = "contact_id")
    @JsonIgnore
    Contact contact;

    public ContactEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Email{" +
                "email='" + email + '\'' +
                '}';
    }
}
