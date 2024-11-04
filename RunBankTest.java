import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class RunBankTest {
    static RunBank bank;

    @BeforeAll
    void setupBeforeAll(){
        bank = new RunBank();
    }

    @Test
    void testCustomerDeposit1(){
        bank.selectAccountAndPerformOperations();
        assertEquals("", );
    }
}
