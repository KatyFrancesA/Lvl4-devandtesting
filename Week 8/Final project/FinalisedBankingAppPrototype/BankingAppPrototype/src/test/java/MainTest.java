/* NO LONGER NEEDED DUE TO REFACTOR

import org.example.BankAccount;
import org.example.CommunityAccount;
import org.example.Main;
import org.example.SmallBusinessAccount;
import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void testAddAccount() {
        Main atm = new Main();
        BankAccount account = new SmallBusinessAccount("Small Business Account", "1", 1);
        atm.addAccount(account);
        assertSame(atm.findAccount("1"), account);
    }

    @Test
    public void testDeposit() {
        Main atm = new Main();
        BankAccount account = new SmallBusinessAccount("Small Business Account", "1", 0); //Changed balance from 1 to 0
        atm.addAccount(account);
        assertTrue(atm.deposit("1", 100.0));
        assertEquals(100.0, atm.checkBalance("1"), 0.001);
    }

    @Test
    public void testCheckBalance() {
        Main atm = new Main();
        BankAccount account = new SmallBusinessAccount("Small Business Account", "1", 0); //Changed balance from 1 to 0
        atm.addAccount(account);
        assertEquals(0.0, atm.checkBalance("1"), 0.001);
        atm.deposit("1", 100.0);
        assertEquals(100.0, atm.checkBalance("1"), 0.001);
    }

    @Test
    public void testFindAccount() {
        Main atm = new Main();
        BankAccount account1 =
                new SmallBusinessAccount("Small Business Account", "1", 1);
        BankAccount account2 = new CommunityAccount("Community Account", "2", 2);
        atm.addAccount(account1);
        atm.addAccount(account2);
        assertSame(atm.findAccount("1"), account1);
        assertSame(atm.findAccount("2"), account2);
        assertNull(atm.findAccount("3"));
    }
} */
