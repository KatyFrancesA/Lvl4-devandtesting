import org.example.BankAccount;
import org.example.CommunityAccount;
import org.junit.Before;
import org.junit.Test;
import java.math.BigDecimal;
import static org.junit.Assert.*;

public class CommunityAccountTest {
    private CommunityAccount account;

    //BigDecimal with correct scale
    private BigDecimal bd(String value) {
        return new BigDecimal(value).setScale(BankAccount.CURRENCY_SCALE, BankAccount.CURRENCY_ROUNDING_MODE);
    }

    @Before
    public void setUp() { account = new CommunityAccount("testowner", "Community Account", "123456789", 5000.0); }

    @Test
    public void testGetOverdraftFacility() { assertEquals(0, bd("2500.00").compareTo(account.getOverdraftFacility())); }


    /* NEWTESTS
    -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */

    @Test
    public void testRequiresSignatoryAuth() {
        assertTrue("CommunityAccount should require signatory auth", account.requiresSignatoryAuth());
    }
}

/*
 * Tests to be added:

 */