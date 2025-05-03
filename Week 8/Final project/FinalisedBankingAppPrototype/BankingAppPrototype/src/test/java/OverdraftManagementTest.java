import org.example.BankAccount;
import org.example.ClientAccount;
import org.example.OverdraftManagement;
import org.example.SmallBusinessAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class OverdraftManagementTest {

    private OverdraftManagement account;

    private BigDecimal bd(String value) {
        return new BigDecimal(value).setScale(BankAccount.CURRENCY_SCALE, BankAccount.CURRENCY_ROUNDING_MODE);
    }

    @BeforeEach
    void setUp() {
        account = new SmallBusinessAccount("testowner", "Overdraft Account", "OD123", 1000.0);
    }

    @Test
    void testWithdraw_WithinBalance() {
        BigDecimal initialBalance = account.getBalance();
        BigDecimal withdrawAmount = bd("500.00");

        boolean success = account.withdraw(withdrawAmount);

        assertTrue(success, "Withdrawal within balance should succeed");
        assertEquals(0, account.getBalance().compareTo(initialBalance.subtract(withdrawAmount)), "Balance should decrease by withdrawal amount");
        assertEquals(0, account.getCurrentOverdraftUsed().compareTo(BigDecimal.ZERO), "Overdraft should not be used");
    }

    @Test
    void testWithdraw_UsingOverdraft() {
        BigDecimal initialBalance = account.getBalance(); //1000.00
        BigDecimal overdraftLimit = account.getOverdraftFacility(); //1000.00 for SmallBusinessAccount
        BigDecimal withdrawAmount = bd("1500.00"); //Withdraw more than balance

        boolean success = account.withdraw(withdrawAmount);

        assertTrue(success, "Withdrawal using overdraft should succeed");
        assertEquals(0, account.getBalance().compareTo(BigDecimal.ZERO), "Balance should become zero");
        assertEquals(0, account.getCurrentOverdraftUsed().compareTo(withdrawAmount.subtract(initialBalance)), "Overdraft used should be the amount beyond initial balance");
    }

    @Test
    void testWithdraw_ExceedingOverdraft() {
        BigDecimal initialBalance = account.getBalance();
        BigDecimal overdraftLimit = account.getOverdraftFacility();
        BigDecimal withdrawAmount = overdraftLimit.add(initialBalance).add(bd("0.01")); //Exceed total available funds

        boolean success = account.withdraw(withdrawAmount);

        assertFalse(success, "Withdrawal exceeding overdraft limit should fail");
        assertEquals(0, account.getBalance().compareTo(initialBalance), "Balance should not change");
        assertEquals(0, account.getCurrentOverdraftUsed().compareTo(BigDecimal.ZERO), "Overdraft used should remain zero");
    }

    @Test
    void testDeposit_RepaysOverdraft() {
        account.withdraw(bd("1500.00"));
        BigDecimal overdraftUsedInitially = account.getCurrentOverdraftUsed();
        BigDecimal depositAmount = bd("300.00");

        account.deposit(depositAmount);

        assertEquals(0, account.getCurrentOverdraftUsed().compareTo(overdraftUsedInitially.subtract(depositAmount)), "Overdraft used should decrease by deposit amount (or become zero)");
        assertEquals(0, account.getBalance().compareTo(BigDecimal.ZERO), "Balance should remain zero (or increase if deposit > overdraft)");
    }

    @Test
    void testDeposit_RepaysOverdraftAndIncreasesBalance() {
        account.withdraw(bd("1500.00"));
        BigDecimal depositAmount = bd("700.00");

        account.deposit(depositAmount);
        assertEquals(0, account.getCurrentOverdraftUsed().compareTo(BigDecimal.ZERO), "Overdraft used should be zero after full repayment");
        assertEquals(0, account.getBalance().compareTo(depositAmount.subtract(bd("500.00"))), "Balance should increase by remaining deposit amount");
    }

    @Test
    void testGetFormattedBalance_Overdrawn() {
        account.withdraw(bd("1500.00"));

        String formattedBalance = account.getFormattedBalance();

        assertTrue(formattedBalance.contains("(Overdrawn)"), "Formatted balance should indicate 'Overdrawn'");
        assertTrue(formattedBalance.contains(BankAccount.currencyFormatter.format(bd("-500.00"))), "Formatted balance should show overdraft amount as negative");
        assertTrue(formattedBalance.contains("Overdraft Remaining"), "Formatted balance should show remaining overdraft");
        assertTrue(formattedBalance.contains(BankAccount.currencyFormatter.format(account.getOverdraftFacility().subtract(bd("500.00")))), "Formatted balance should show correct remaining overdraft");
    }

    @Test
    void testTransferUsingOverdraft() {
        BankAccount toAccount = new ClientAccount("testowner", "Test Client Acc", "0987654321", 500.0);
        BigDecimal initialToBalance = toAccount.getBalance();
        BigDecimal transferAmount = bd("1200.00");

        boolean success = account.transferTo(toAccount, transferAmount);

        assertTrue(success, "Transfer using overdraft should succeed");
        assertEquals(0, account.getBalance().compareTo(BigDecimal.ZERO), "From account balance should be zero");
        assertEquals(0, account.getCurrentOverdraftUsed().compareTo(bd("200.00")), "Overdraft used should be 200");
        assertEquals(0, toAccount.getBalance().compareTo(initialToBalance.add(transferAmount)), "To account balance should increase");
    }
}