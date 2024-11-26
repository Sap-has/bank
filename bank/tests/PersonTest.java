package tests;

import bank.Person;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PersonTest {
    static Person customer;
    @BeforeAll
    static void setupEach(){
        customer = new Person("Daniel","A","5-Mar-39","500 W. University Ave, El Paso, TX 79968","(915) 747-5042");
    }

    @Test
    void testGetAddress() {
        assertEquals("500 W. University Ave, El Paso, TX 79968", customer.getAddress());
    }

    @Test
    void testGetDateOfBirth() {
        assertEquals("5-Mar-39", customer.getDateOfBirth());
    }

    @Test
    void testGetFirstName() {
        assertEquals("Daniel", customer.getFirstName());
    }

    @Test
    void testGetLastName() {
        assertEquals("A", customer.getLastName());
    }

    @Test
    void testGetName() {
        assertEquals("Daniel A", customer.getName());
    }

    @Test
    void testGetPhoneNum() {
        assertEquals("(915) 747-5042", customer.getPhoneNum());
    }

    @AfterAll
    static void tearDown(){
        customer = null;
    }
}
