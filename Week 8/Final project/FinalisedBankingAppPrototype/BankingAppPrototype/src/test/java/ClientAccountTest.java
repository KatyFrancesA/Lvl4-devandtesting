import org.example.ClientAccount;
import org.junit.Before;
import org.junit.Test;
import java.math.BigDecimal;
import static org.junit.Assert.*;

public class ClientAccountTest {
    private ClientAccount account;

    //BigDecimal with correct scale
    private BigDecimal bd(String value) {
        return new BigDecimal(value).setScale(ClientAccount.CURRENCY_SCALE, ClientAccount.CURRENCY_ROUNDING_MODE);
    }

    @Before
    public void setUp() { account = new ClientAccount("testowner", "John Smith", "1234567890", 1000.0); }

    @Test
    public void testGetOverdraftFacility() { assertEquals(0, bd("1500.00").compareTo(account.getOverdraftFacility())); }

    /* NEWTESTS
    -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */

    @Test
    public void testRequiresSignatoryAuth() {
        assertFalse("ClientAccount should not require signatory auth", account.requiresSignatoryAuth());
    }
}

/*
 * Tests to be added:

 */