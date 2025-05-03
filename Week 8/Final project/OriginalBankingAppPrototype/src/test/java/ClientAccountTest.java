import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ClientAccountTest {

    private ClientAccount account;

    @Before
    public void setUp() {
        account = new ClientAccount("John Smith", "1234567890", 1000.0);
    }

    @Test
    public void testGetOverdraftFacility() {
        double expected = 1500.0;
        double actual = account.getOverdraftFacility();
        assertEquals(expected, actual, 0.0);
    }
}
