import org.example.SmallBusinessAccount;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SmallBusinessAccountTest {
    private SmallBusinessAccount account;
    private final String owner = "Jane Doe";

    //BigDecimal with correct scale
    private BigDecimal bd(String value) {
        return new BigDecimal(value).setScale(SmallBusinessAccount.CURRENCY_SCALE, SmallBusinessAccount.CURRENCY_ROUNDING_MODE);
    }

    @BeforeEach
    public void setUp() {
        account = new SmallBusinessAccount(owner, "Small Business Account",
                "123456789", 2000.0);
    }

    @Test
    public void testGetOverdraftFacility() { assertEquals(bd("1000.00"), account.getOverdraftFacility()); }



    /* NEWTESTS
    -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */

    @Test
    public void testRequiresSignatoryAuth() {
        assertNotSame("SmallBusinessAccount should not require signatory auth", account.requiresSignatoryAuth());
    }

}

/*
 * Tests to be added:

 */
