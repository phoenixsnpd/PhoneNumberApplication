package com.example.phonenumberapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "phone_numbers")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class ContactPhoneNumber {
    @Id
    @Column(name = "phone_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    long id;
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    @Column(name = "number")
    String phoneNumber;
    @ManyToOne
    @JoinColumn(name = "contact_id")
    @JsonIgnore
    Contact contact;

    public ContactPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "PhoneNumber{" +
                "phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
