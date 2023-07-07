package com.example.phonenumberapp;
import com.example.phonenumberapp.entity.Contact;
import com.example.phonenumberapp.entity.ContactEmail;
import com.example.phonenumberapp.entity.ContactPhoneNumber;
import com.example.phonenumberapp.entity.User;
import com.example.phonenumberapp.repository.ContactRepository;
import com.example.phonenumberapp.repository.UserRepository;
import com.example.phonenumberapp.service.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContactServiceTest {

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ContactService contactService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddContact() {
        String username = "testUser";
        User user = new User(username, "password");
        Contact contact = new Contact();
        contact.setName("Unknown User");

        when(userRepository.findByName(username)).thenReturn(Optional.of(user));
        when(contactRepository.save(contact)).thenReturn(contact);

        Contact addedContact = contactService.addContact(username, contact);

        assertNotNull(addedContact);
        assertEquals(user, addedContact.getUser());
        verify(userRepository, times(1)).findByName(username);
        verify(contactRepository, times(1)).save(contact);
    }

    @Test
    void shouldCheckIfContactHasChangedSinceUpdate() {
        Contact contact = new Contact();
        contact.setName("New Contact");
        contact.setEmails(Arrays.asList(new ContactEmail("new@example.com")));
        contact.setContactPhoneNumbers(Arrays.asList(new ContactPhoneNumber("+380939333333")));
        contact.setUser(new User("User", "password"));
        when(contactRepository.findById(anyLong())).thenReturn(Optional.of(contact));

        Contact updatedContact = new Contact();
        updatedContact.setId(1L);
        updatedContact.setName("Changed User");
        updatedContact.setEmails(Arrays.asList(new ContactEmail("chnhe@example.com")));
        updatedContact.setContactPhoneNumbers(Arrays.asList(new ContactPhoneNumber("+38093944444")));
        updatedContact.setUser(new User("New User", "password"));

        Contact editedContact = contactService.editContact(1L, updatedContact);

        assertNotNull(editedContact);
        assertEquals(updatedContact.getName(), editedContact.getName());
        assertEquals(updatedContact.getEmails(), editedContact.getEmails());
        assertEquals(updatedContact.getContactPhoneNumbers(), editedContact.getContactPhoneNumbers());
        assertEquals(updatedContact.getUser(), editedContact.getUser());
        verify(contactRepository, times(1)).findById(1L);
        verify(contactRepository, times(1)).save(contact); // Исправлено сохранение контакта
    }

    @Test
    void shouldThrowRuntimeExceptionIfNoContactIsFound() {
        long nonExistingContactId = 999999999L;
        Contact updatedContact = new Contact();
        updatedContact.setId(nonExistingContactId);
        updatedContact.setName("Unknown User");
        updatedContact.setEmails(Arrays.asList(new ContactEmail("unknown@example.com")));
        updatedContact.setContactPhoneNumbers(Arrays.asList(new ContactPhoneNumber("9876543210")));
        updatedContact.setUser(new User("Unknown User", "password"));

        assertThrows(RuntimeException.class, () -> contactService.editContact(nonExistingContactId, updatedContact));
    }

    @Test
    void ShouldDeleteContact() {
        Contact contact = new Contact();
        contact.setId(1L);
        contact.setName("Test Contact");

        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));

        Contact deletedContact = contactService.deleteContact(1L);

        // Проверяем результаты
        assertNotNull(deletedContact);
        assertEquals(contact.getId(), deletedContact.getId());
        assertEquals(contact.getName(), deletedContact.getName());
        verify(contactRepository, times(1)).findById(1L);
        verify(contactRepository, times(1)).delete(contact);
    }

    @Test
    void shouldCheckIfContactWasDeleted() {
        when(contactRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> contactService.deleteContact(1L));

        verify(contactRepository, times(1)).findById(1L);
        verify(contactRepository, never()).delete(any(Contact.class));
    }

    @Test
    void testGetAllContacts() {
        String username = "testUser";
        User user = new User(username, "password");

        List<Contact> contacts = new ArrayList<>();
        Contact contact1 = new Contact();
        contact1.setId(1L);
        contact1.setName("Contact 1");
        contact1.setUser(user);
        contacts.add(contact1);
        Contact contact2 = new Contact();
        contact2.setId(2L);
        contact2.setName("Contact 2");
        contact2.setUser(user);
        contacts.add(contact2);

        when(userRepository.findByName(username)).thenReturn(Optional.of(user));

        when(contactRepository.findAllByUser(user)).thenReturn(contacts);

        List<Contact> resultContacts = contactService.getAllContacts(username);

        assertNotNull(resultContacts);
        assertEquals(contacts.size(), resultContacts.size());
        assertEquals(contacts, resultContacts);
        verify(userRepository, times(1)).findByName(username);
        verify(contactRepository, times(1)).findAllByUser(user);
    }

    @Test
    void shouldReturnTrueIfEmailsAreUnique() {
        List<ContactEmail> emails = new ArrayList<>();
        emails.add(new ContactEmail("email1@example.com"));
        emails.add(new ContactEmail("email2@example.com"));
        emails.add(new ContactEmail("email3@example.com"));

        boolean isUnique = contactService.isEmailUniqueForContact(emails);

        assertTrue(isUnique);
    }

    @Test
    void shouldReturnFalseIfPhoneNumbersAreDifferent() {
        List<ContactPhoneNumber> phoneNumbers = new ArrayList<>();
        phoneNumbers.add(new ContactPhoneNumber("9876543210"));
        phoneNumbers.add(new ContactPhoneNumber("9876543210"));
        phoneNumbers.add(new ContactPhoneNumber("5555555555"));

        boolean isUnique = contactService.isPhoneNumberUniqueForContact(phoneNumbers);

        assertFalse(isUnique);
    }
}