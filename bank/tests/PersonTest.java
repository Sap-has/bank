package tests;

import bank.Person;
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

    }

    @Test
    void testGetDateOfBirth() {

    }

    @Test
    void testGetFirstName() {

    }

    @Test
    void testGetLastName() {

    }

    @Test
    void testGetName() {

    }

    @Test
    void testGetPhoneNum() {

    }

    @AfterAll
    static void tearDown(){
        customer = null;
    }
}
