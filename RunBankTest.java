import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class RunBankTest {
    private RunBank runBank;
    private Account testAccount;

    @BeforeEach
    void setUp() {
        runBank = new RunBank();
        
        // Initialize a test account with a sample balance
        testAccount = new Checking(null, null, 1000.00, 0); // Assume Checking is a subclass of Account
    }

    @Test
    void testDeposit() {
        double depositAmount = 150.00;

        // Perform the deposit operation
        runBank.handleTransaction(testAccount, depositAmount, true);

        // Check if the updated balance is correct
        assertEquals(1150.00, testAccount.getBalance(), "The balance should be updated correctly after the deposit.");
    }
}
