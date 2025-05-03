import org.example.BankAccount;
import org.example.ClientAccount;
import org.example.SmallBusinessAccount;
import org.junit.Before;
import org.junit.Test;
import java.math.BigDecimal;
import static org.junit.Assert.*;

public class BankAccountTest {
    private SmallBusinessAccount account;
    private BankAccount toAccount;

    //BigDecimal with correct scale
    private BigDecimal bd(String value) {
        return new BigDecimal(value).setScale(SmallBusinessAccount.CURRENCY_SCALE, SmallBusinessAccount.CURRENCY_ROUNDING_MODE);
    }

    @Before
    public void setUp() {
        account = new SmallBusinessAccount("testowner", "John Smith", "1234567890", 1000.0);
        toAccount = new ClientAccount("testowner", "Jane Doe", "0987654321", 500.0);
    }

    @Test
    public void testDeposit() {
        account.deposit(bd("500.00"));
        assertEquals("Balance should be 1500.00 after deposit", 0, bd("1500.00").compareTo(account.getBalance()));
    }

    @Test
    public void testWithdraw() { //No overdraft
        boolean success = account.withdraw(bd("1000.00"));
        assertTrue("Withdrawal of exact balance should succeed", success);
        assertEquals("Balance should be 0.00 after withdrawing exact amount", 0, bd("0.00").compareTo(account.getBalance()));
    }

    @Test
    public void testWithdrawInsufficientFunds() { //No overdraft
        boolean success = account.withdraw(bd("2000.01"));
        assertFalse("Withdrawal exceeding balance and overdraft should fail", success);
        assertEquals("Balance should remain 1000.00", 0, bd("1000.00").compareTo(account.getBalance()));
    }


    @Test
    public void testTransfer() {
        boolean success = account.transferTo(toAccount, bd("500.00"));
        assertTrue("Transfer should succeed", success);
        assertEquals("From account balance should be 500.00", 0, bd("500.00").compareTo(account.getBalance()));
        assertEquals("To account balance should be 1000.00", 0, bd("1000.00").compareTo(toAccount.getBalance()));
    }

    @Test
    public void testTransferInsufficientFunds() {
        boolean success = account.transferTo(toAccount, bd("2000.01"));
        assertFalse("Transfer exceeding balance and overdraft should fail", success);
        assertEquals("From account balance should remain 1000.00", 0, bd("1000.00").compareTo(account.getBalance()));
        assertEquals("To account balance should remain 500.00", 0, bd("500.00").compareTo(toAccount.getBalance()));
    }

    @Test
    public void testGetAccountName() { assertEquals("Account name should match", "John Smith", account.getAccountName()); }

    @Test
    public void testGetAccountNumber() { assertEquals("Account number should match", "1234567890", account.getAccountNumber()); }

    @Test
    public void testTwoSignatoriesRestriction() {
        assertFalse("SmallBusinessAccount should not require signatory auth", account.requiresSignatoryAuth()); }

    @Test
    public void testDepositZeroOrNegative() {
        BigDecimal initialBalance = account.getBalance();
        account.deposit(bd("0.00"));
        assertEquals("Balance should not change on zero deposit", 0, initialBalance.compareTo(account.getBalance()));
        account.deposit(bd("-50.00"));
        assertEquals("Balance should not change on negative deposit", 0, initialBalance.compareTo(account.getBalance()));
    }

    @Test
    public void testWithdrawUsingOverdraft() {
        boolean success = account.withdraw(bd("1500.00"));
        assertTrue("Withdrawal using overdraft should succeed", success);
        assertEquals("Balance should be zero", 0, bd("0.00").compareTo(account.getBalance()));
    }


    /* NEWTESTS
    -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */

    @Test
    public void testWithdrawZeroOrNegative() {
        BigDecimal initialBalance = account.getBalance();
        boolean successZero = account.withdraw(bd("0.00"));
        assertFalse("Zero withdrawal should fail/return false", successZero);
        assertEquals("Balance should not change on zero withdrawal", 0, initialBalance.compareTo(account.getBalance()));

        boolean successNegative = account.withdraw(bd("-50.00"));
        assertFalse("Negative withdrawal should fail/return false", successNegative);
        assertEquals("Balance should not change on negative withdrawal", 0, initialBalance.compareTo(account.getBalance()));
    }
}



/*
 * Tests to be added:

 */