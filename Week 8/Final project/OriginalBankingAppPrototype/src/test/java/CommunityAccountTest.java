import static org.junit.Assert.*;
import org.junit.*;

public class CommunityAccountTest {
    private CommunityAccount account;

    @Before
    public void setUp() {
        account = new CommunityAccount("Community Account", "123456789", 5000.0);
    }

    @Test
    public void testGetOverdraftFacility() {
        assertEquals(2500.0, account.getOverdraftFacility(), 0.001);
    }
}
