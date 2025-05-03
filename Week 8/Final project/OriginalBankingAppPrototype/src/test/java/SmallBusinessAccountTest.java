import static org.junit.Assert.*;
import org.junit.*;

public class SmallBusinessAccountTest {
    private SmallBusinessAccount account;

    @Before
    public void setUp() {
        account = new SmallBusinessAccount("Small Business Account",
                "123456789", 2000.0);
    }

    @Test
    public void testGetOverdraftFacility() {
        assertEquals(1000.0, account.getOverdraftFacility(), 0.001);
    }
}
