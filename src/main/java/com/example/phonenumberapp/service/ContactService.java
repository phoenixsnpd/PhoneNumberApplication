package com.example.phonenumberapp.service;

import com.example.phonenumberapp.entity.Contact;
import com.example.phonenumberapp.entity.ContactEmail;
import com.example.phonenumberapp.entity.ContactPhoneNumber;
import com.example.phonenumberapp.entity.User;
import com.example.phonenumberapp.repository.ContactRepository;
import com.example.phonenumberapp.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ContactService {
    private final ContactRepository contactRepository;
    private final UserRepository userRepository;

    public ContactService(ContactRepository contactRepository, UserRepository userRepository) {
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
    }

    public Contact addContact(String username, Contact contact) {
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        contact.setUser(user);
        return contactRepository.save(contact);
    }

    public Contact editContact(long contactID, Contact updatedContact) {
        Optional<Contact> optionalContact = contactRepository.findById(contactID);
        if (optionalContact.isPresent()) {
            Contact contact = optionalContact.get();
            contact.setName(updatedContact.getName());
            contact.setEmails(updatedContact.getEmails());
            contact.setContactPhoneNumbers(updatedContact.getContactPhoneNumbers());
            contact.setUser(updatedContact.getUser());
            contactRepository.save(contact);

            return contact;
        } else {
            throw new RuntimeException("Contact with the specified Id not found.");
        }
    }

    public Contact deleteContact(long contactID) {
        Optional<Contact> optionalContact = contactRepository.findById(contactID);
        if (optionalContact.isPresent()) {
            contactRepository.delete(optionalContact.get());
            return optionalContact.get();
        } else {
            throw new RuntimeException("Contact with the specified Id not found.");
        }
    }

    public List<Contact> getAllContacts(String username) {
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return contactRepository.findAllByUser(user);
    }

    public boolean isEmailUniqueForContact(List<ContactEmail> emails) {
        Set<String> uniqueEmails = new HashSet<>();
        for (ContactEmail email : emails) {
            String emailValue = email.getEmail();
            if (uniqueEmails.contains(emailValue)) {
                return false;
            }
            uniqueEmails.add(emailValue);
        }
        return true;
    }

    public boolean isPhoneNumberUniqueForContact(List<ContactPhoneNumber> phoneNumbers) {
        // Проверка уникальности номеров телефона в пределах контакта
        Set<String> uniquePhoneNumbers = new HashSet<>();
        for (ContactPhoneNumber phoneNumber : phoneNumbers) {
            String phoneNumberValue = phoneNumber.getPhoneNumber();
            if (uniquePhoneNumbers.contains(phoneNumberValue)) {
                return false;
            }
            uniquePhoneNumbers.add(phoneNumberValue);
        }
        return true;
    }
}

