package test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import genbank.AccountOperations;

public class AccountOperationsTest {
    AccountOperations acc;

    @BeforeAll
    void setUpAll(){
        acc = new AccountOperations();
    }

    @Test
    void testCalculateCreditLimit() {
        assertEquals(600, acc.calculateCreditLimit(580));
    }

    @Test
    void testSelectOperation() {
        assertEquals("Transfer", acc.selectOperation("4"));
    }
}
