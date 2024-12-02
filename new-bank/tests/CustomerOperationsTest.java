package tests;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import 
import CustomerOperations;


public class CustomerOperationsTest {
    private HashMap<Integer, Customer> bankUserCustomer;
    private CustomerOperations customerOperations;

    @BeforeEach
    void setUp() {
        // Initialize CustomerOperations instance
        customerOperations = new CustomerOperations();

        // Populate the static bankUserCustomer map
        bankUserCustomer = customerOperations.bankUserCustomer;
        bankUserCustomer.put(79, new Customer("Daniel", "A", "5-Mar-39", "500 W. University Ave, El Paso, TX 79968", "(915) 747-5042", 79, "1078", 857.56, "2078", 3364.79, "3078", 7985, -786.93));
        bankUserCustomer.put(49, new Customer("Derek", "Aguirre", "5-May-41", "500 W. University Ave, El Paso, TX 79968", "(915) 747-5012", 49, "1048", 1845.56, "2048", 3352.49, "3048", 8899, -58.57));
    }

    @Test
    void testOpenAccounts() {
        // Test a valid customerId
        String name = customerOperations.openAccounts(79);
        assertEquals("Daniel A", name, "Should return the full name for valid customer ID 79");
    }
}