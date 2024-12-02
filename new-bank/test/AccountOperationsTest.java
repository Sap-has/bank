package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import genbank.AccountOperations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;

public class AccountOperationsTest {
    private AccountOperations accountOperations; // Instantiate the class
    private HashMap<Integer, String[]> bankUsers;
    
    @BeforeEach
    void setUp() {
        accountOperations = new AccountOperations();
        bankUsers = accountOperations.bankUsers;
        String[] arr = {"79","Daniel","A","5-Mar-39","500 W. University Ave, El Paso, TX 79968","(915) 747-5042","1078","857.56","2078","3364.79","3078","7985","-786.93"};
        bankUsers.put(79, arr);
    }

    @Test
    public void testSelectOperationValidInputs() {
        // Test for each valid input
        assertEquals("Inquire Balance", accountOperations.selectOperation("1"));
        assertEquals("Deposit", accountOperations.selectOperation("2"));
        assertEquals("Withdraw", accountOperations.selectOperation("3"));
        assertEquals("Transfer", accountOperations.selectOperation("4"));
        assertEquals("Generate Transaction file", accountOperations.selectOperation("5"));
        assertEquals("exit", accountOperations.selectOperation("exit"));
    }

    @Test
    public void testSelectOperationInvalidInput() {
        // Test for an invalid input
        assertEquals("-1", accountOperations.selectOperation("invalid"));
    }

    @Test
    public void testSelectOperationCaseSensitivity() {
        // Test for case sensitivity; input should be normalized to lowercase
        assertEquals("Inquire Balance", accountOperations.selectOperation("1"));
        assertEquals("Deposit", accountOperations.selectOperation("2"));
        assertEquals("Withdraw", accountOperations.selectOperation("3"));
        assertEquals("Transfer", accountOperations.selectOperation("4"));
        assertEquals("Generate Transaction file", accountOperations.selectOperation("5"));
        assertEquals("exit", accountOperations.selectOperation("EXIT"));
    }
}
