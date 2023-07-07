package com.example.phonenumberapp.controllers;

import com.example.phonenumberapp.dto.ContactDTO;
import com.example.phonenumberapp.entity.Contact;
import com.example.phonenumberapp.entity.ContactEmail;
import com.example.phonenumberapp.entity.ContactPhoneNumber;
import com.example.phonenumberapp.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class ContactController {
    ContactService service;

    public ContactController(ContactService service) {
        this.service = service;
    }

    @PutMapping("/contacts")
    public ResponseEntity<String> addNewContact(Authentication auth, @Valid @RequestBody ContactDTO newContact) {
        Contact contact = convertDTOToContact(newContact);
        if (!service.isEmailUniqueForContact(contact.getEmails())) {
            return ResponseEntity.badRequest().body("Emails must be unique within the contact");
        }

        if (!service.isPhoneNumberUniqueForContact(contact.getContactPhoneNumbers())) {
            return ResponseEntity.badRequest().body("Phone numbers must be unique within the contact");
        }
        service.addContact(auth.getName(), contact);
        return ResponseEntity.ok("Contact created successfully");
    }

    @DeleteMapping("/contacts/{id}")
    public ResponseEntity<String> deleteContactByID(@PathVariable long id) {
        service.deleteContact(id);
        return ResponseEntity.ok("Contact deleted successfully");
    }

    @PostMapping("contacts/{id}")
    public ResponseEntity<String> editContact(@RequestBody ContactDTO updatedContact, @PathVariable long id) {
        Contact newContact = convertDTOToContact(updatedContact);
        service.editContact(id, newContact);
        return ResponseEntity.ok("Contact edited successfully");
    }

    @GetMapping("/contacts")
    public List<Contact> getAllContacts(Authentication auth) {
        return service.getAllContacts(auth.getName());
    }


    private Contact convertDTOToContact(ContactDTO contactDTO) {
        Contact contact = new Contact();
        contact.setName(contactDTO.getName());

        List<ContactEmail> emails = contactDTO.getEmails().stream()
                .map(ContactEmail::new)
                .collect(Collectors.toList());
        contact.setEmails(emails);

        List<ContactPhoneNumber> contactPhoneNumbers = contactDTO.getPhones().stream()
                .map(ContactPhoneNumber::new)
                .collect(Collectors.toList());
        contact.setContactPhoneNumbers(contactPhoneNumbers);

        return contact;
    }
}
