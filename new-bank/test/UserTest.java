package test;

import genbank.User;
import genbank.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;

public class UserTest {
    private User user;
    private HashMap<Integer, String[]> bankUsers;

    @BeforeEach
    void setUp() {
        user = new User();
        bankUsers = user.bankUsers;
        String[] arr = {"79", "Daniel", "A", "5-Mar-39", "500 W. University Ave, El Paso, TX 79968", "(915) 747-5042", "1078", "857.56", "2078", "3364.79", "3078", "7985", "-786.93"};
        bankUsers.put(79, arr);
        
        // Add a mock Customer to `bankUserCustomer` for testing
        Customer mockCustomer = new Customer("Daniel", "A", "5-Mar-39", "500 W. University Ave, El Paso, TX 79968", "(915) 747-5042", 79, "1078", 857.56, "2078", 3364.79, "3078", 7985, -786.93);
        user.bankUserCustomer.put(79, mockCustomer);
        
        // Add name to `bankuserNames` mapping for testing name lookup
        user.bankuserNames.put("daniel a", 79);
    }

    @Test
    public void testFetchCustomerById() {
        Customer result = user.fetchCustomer("79");
        assertNotNull(result, "The method should return a valid Customer when a valid ID is provided.");
        assertEquals(79, result.getCustomerID(), "The customer ID should match the input.");
    }

    @Test
    public void testFetchCustomerByName() {
        Customer result = user.fetchCustomer("Daniel A");
        assertNotNull(result, "The method should return a valid Customer when a valid full name is provided.");
        assertEquals(79, result.getCustomerID(), "The customer ID should match the input.");
    }

    @Test
    public void testFetchCustomerInvalidId() {
        Customer result = user.fetchCustomer("100");
        assertNull(result, "The method should return null when an invalid ID is provided.");
    }

    @Test
    public void testFetchCustomerInvalidName() {
        Customer result = user.fetchCustomer("John Doe");
        assertNull(result, "The method should return null when an invalid name is provided.");
    }

    @Test
    public void testFetchCustomerPartialInput() {
        Customer result = user.fetchCustomer("daniel");
        assertNull(result, "The method should return null if a partial name input is given without a full match.");
    }
}
