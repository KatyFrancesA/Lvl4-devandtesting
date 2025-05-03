import org.example.*;
import org.junit.Before;
import org.junit.Test;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.Assert.*;

//Replacing Main Test
//Had to refactor some of the original tests as some logic has been changed, e.g double to BigDecimal for money
public class AccountServiceTest {
    private UserService userService;
    private AccountService accountService;
    private final String testUser = "testowner";
    private final String testUser2 = "anotherowner";

    //BigDecimal with correct scale
    private BigDecimal bd(String value) {
        return new BigDecimal(value).setScale(BankAccount.CURRENCY_SCALE, BankAccount.CURRENCY_ROUNDING_MODE);
    }

    @Before
    public void setUp() {
        userService = new UserService();
        AccountService.resetNextAccountNumberForTest(); //Reset counter
        accountService = new AccountService(userService);
        userService.createUser("Test Owner", testUser, "Password123!");
        userService.createUser("Another Owner", testUser2, "Password456!");
    }

    @Test
    public void testCreateAccount() { //Renamed test from addAccount to reflect AccountService.createAccount
        BankAccount createdAccount = accountService.createAccount(testUser, AccountService.TYPE_SMALL_BUSINESS, "Small Business Account", 0.0);
        assertNotNull("Account creation should return a non-null account", createdAccount);
        Optional<BankAccount> foundAccountOpt = accountService.findAccountForUser("1", testUser);
        assertTrue("Account should be found in AccountService after creation", foundAccountOpt.isPresent());
        assertSame("Found account should be the same instance as created", createdAccount, foundAccountOpt.get());
    }

    @Test
    public void testDeposit() {
        BankAccount account = accountService.createAccount(testUser, AccountService.TYPE_CLIENT, "Small Business Account", 50.0);
        assertNotNull(account);
        String accNum = account.getAccountNumber();

        boolean success = accountService.deposit(accNum, bd("100.50"), testUser);
        assertTrue("Deposit should succeed", success);

        Optional<BankAccount> updatedAccountOpt = accountService.findAccountForUser(accNum, testUser);
        assertTrue(updatedAccountOpt.isPresent());
        assertEquals("Balance should be updated after deposit", 0, bd("150.50").compareTo(updatedAccountOpt.get().getBalance()));

        boolean failDeposit = accountService.deposit(accNum, bd("10.00"), testUser2);
        assertFalse("Deposit should fail for non-owner", failDeposit);
        assertEquals("Balance should not change after failed deposit", 0, bd("150.50").compareTo(updatedAccountOpt.get().getBalance()));

        boolean failZeroDeposit = accountService.deposit(accNum, bd("0.00"), testUser);

        assertEquals("Balance should not change after zero deposit", 0, bd("150.50").compareTo(updatedAccountOpt.get().getBalance()));

        boolean failNegativeDeposit = accountService.deposit(accNum, bd("-10.00"), testUser);
        assertEquals("Balance should not change after negative deposit", 0, bd("150.50").compareTo(updatedAccountOpt.get().getBalance()));

    }

    @Test
    public void testCheckBalance() {
        BankAccount account = accountService.createAccount(testUser, AccountService.TYPE_SMALL_BUSINESS, "SB Acc", 0.0);
        assertNotNull(account);
        String accNum = account.getAccountNumber();

        String balanceStr1 = accountService.checkBalance(accNum, testUser);
        assertTrue("Balance string should contain £0.00", balanceStr1.contains(BankAccount.currencyFormatter.format(bd("0.00"))));
        assertTrue("Balance string should contain overdraft info", balanceStr1.contains("Overdraft Remaining"));

        accountService.deposit(accNum, bd("100.00"), testUser);

        String balanceStr2 = accountService.checkBalance(accNum, testUser);
        assertTrue("Balance string should contain £100.00", balanceStr2.contains(BankAccount.currencyFormatter.format(bd("100.00"))));
        assertTrue("Balance string should still contain overdraft info", balanceStr2.contains("Overdraft Remaining"));

        String balanceStr3 = accountService.checkBalance(accNum, testUser2);
        assertTrue("Check balance for non-owner should fail", balanceStr3.contains("not found or not owned"));

        String balanceStr4 = accountService.checkBalance("99", testUser);
        assertTrue("Check balance for non-existent account should fail", balanceStr4.contains("not found or not owned"));
    }

    @Test
    public void testFindAccount() {
        BankAccount createdAccount = accountService.createAccount(testUser, AccountService.TYPE_SMALL_BUSINESS, "My SB Account", 0.0);
        Optional<BankAccount> foundOpt = accountService.findAccountForUser("1", testUser);
        assertTrue("Account should be found for owner", foundOpt.isPresent());
        assertSame("Found account should be the same instance", createdAccount, foundOpt.get());

        Optional<BankAccount> notFoundOpt = accountService.findAccountForUser("1", testUser2);
        assertFalse("Account should not be found for different owner", notFoundOpt.isPresent());

        Optional<BankAccount> nonExistentOpt = accountService.findAccountForUser("99", testUser);
        assertFalse("Non-existent account should not be found", nonExistentOpt.isPresent());
    }

    @Test
    public void testAccountTypeExistsForUser() {
        accountService.createAccount(testUser, AccountService.TYPE_SMALL_BUSINESS, "SB 1", 100.0);
        accountService.createAccount(testUser, AccountService.TYPE_CLIENT, "Client 1", 200.0);
        accountService.createAccount(testUser2, AccountService.TYPE_COMMUNITY, "Comm 1", 300.0);

        assertTrue("User should have Small Business", accountService.accountTypeExistsForUser(testUser, AccountService.TYPE_SMALL_BUSINESS));
        assertTrue("User should have Client", accountService.accountTypeExistsForUser(testUser, AccountService.TYPE_CLIENT));
        assertFalse("User should NOT have Community", accountService.accountTypeExistsForUser(testUser, AccountService.TYPE_COMMUNITY));

        assertFalse("User2 should NOT have Small Business", accountService.accountTypeExistsForUser(testUser2, AccountService.TYPE_SMALL_BUSINESS));
        assertTrue("User2 should have Community", accountService.accountTypeExistsForUser(testUser2, AccountService.TYPE_COMMUNITY));
    }


    /* NEWTESTS
    -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */

    @Test
    public void testFindAccountForUser() {
        BankAccount account1 = accountService.createAccount(testUser, AccountService.TYPE_SMALL_BUSINESS, "SB 1", 100.0);
        BankAccount account2 = accountService.createAccount(testUser, AccountService.TYPE_CLIENT, "Client 1", 200.0);
        BankAccount account3 = accountService.createAccount(testUser2, AccountService.TYPE_COMMUNITY, "Comm 1", 300.0);

        assertNotNull(account1);
        assertNotNull(account2);
        assertNotNull(account3);

        Optional<BankAccount> found1Opt = accountService.findAccountForUser(account1.getAccountNumber(), testUser);
        assertTrue(found1Opt.isPresent());
        assertSame(account1, found1Opt.get());

        Optional<BankAccount> found2Opt = accountService.findAccountForUser(account2.getAccountNumber(), testUser);
        assertTrue(found2Opt.isPresent());
        assertSame(account2, found2Opt.get());

        Optional<BankAccount> found3Opt_wrongUser = accountService.findAccountForUser(account3.getAccountNumber(), testUser);
        assertFalse(found3Opt_wrongUser.isPresent());

        Optional<BankAccount> found3Opt_correctUser = accountService.findAccountForUser(account3.getAccountNumber(), testUser2);
        assertTrue(found3Opt_correctUser.isPresent());
        assertSame(account3, found3Opt_correctUser.get());

        Optional<BankAccount> nonExistentOpt = accountService.findAccountForUser("99", testUser);
        assertFalse(nonExistentOpt.isPresent());
    }

    @Test
    public void testTransferToDifferentOwnerFails() {
        //Accounts owned by different users
        BankAccount fromAccount = accountService.createAccount(testUser, AccountService.TYPE_CLIENT, "From Acc", 1000.0);
        BankAccount toAccount = accountService.createAccount(testUser2, AccountService.TYPE_SMALL_BUSINESS, "To Acc", 500.0);
        assertNotNull(fromAccount);
        assertNotNull(toAccount);

        //Attempt transfer AccountService should prevent this as 'toAccount' isn't owned by 'testUser'
        boolean success = accountService.transfer(fromAccount.getAccountNumber(), toAccount.getAccountNumber(), bd("300.00"), testUser);
        assertFalse("Transfer to different owner should fail", success);

        //Verify balances haven't changed
        Optional<BankAccount> updatedFromOpt = accountService.findAccountForUser(fromAccount.getAccountNumber(), testUser);
        Optional<BankAccount> updatedToOpt = accountService.findAccountForUser(toAccount.getAccountNumber(), testUser2); //Check owner 2
        assertTrue(updatedFromOpt.isPresent());
        assertTrue(updatedToOpt.isPresent());

        assertEquals("From account balance should not change", 0, bd("1000.00").compareTo(updatedFromOpt.get().getBalance()));
        assertEquals("To account balance should not change", 0, bd("500.00").compareTo(updatedToOpt.get().getBalance()));
    }

    @Test
    public void testWithdrawUsingOverdraft() {
        BankAccount account = accountService.createAccount(testUser, AccountService.TYPE_SMALL_BUSINESS, "SB Acc", 500.0);
        assertNotNull(account);
        String accNum = account.getAccountNumber();

        boolean success = accountService.withdraw(accNum, bd("800.00"), testUser);
        assertTrue("Withdrawal using overdraft should succeed", success);

        Optional<BankAccount> updatedAccountOpt = accountService.findAccountForUser(accNum, testUser);
        assertTrue(updatedAccountOpt.isPresent());
        assertEquals("Balance should be zero after using overdraft", 0, bd("0.00").compareTo(updatedAccountOpt.get().getBalance()));
        if (updatedAccountOpt.get() instanceof OverdraftManagement) {
            OverdraftManagement odAccount = (OverdraftManagement) updatedAccountOpt.get();
            assertEquals("Overdraft used should be correct", 0, bd("300.00").compareTo(odAccount.getCurrentOverdraftUsed()));
        } else {
            fail("Account should be instance of OverdraftManagement");
        }
    }

    @Test
    public void testWithdrawExceedingOverdraft() {
        BankAccount account = accountService.createAccount(testUser, AccountService.TYPE_SMALL_BUSINESS, "SB Acc", 500.0);
        assertNotNull(account);
        String accNum = account.getAccountNumber();

        boolean success = accountService.withdraw(accNum, bd("1500.01"), testUser);
        assertFalse("Withdrawal exceeding overdraft should fail", success);

        Optional<BankAccount> updatedAccountOpt = accountService.findAccountForUser(accNum, testUser);
        assertTrue(updatedAccountOpt.isPresent());
        assertEquals("Balance should not change", 0, bd("500.00").compareTo(updatedAccountOpt.get().getBalance()));
        if (updatedAccountOpt.get() instanceof OverdraftManagement) {
            OverdraftManagement odAccount = (OverdraftManagement) updatedAccountOpt.get();
            assertEquals("Overdraft used should be zero", 0, bd("0.00").compareTo(odAccount.getCurrentOverdraftUsed()));
        }
    }

    @Test
    public void testCloseAccountValid() {
        BankAccount account = accountService.createAccount(testUser, AccountService.TYPE_CLIENT, "To Close", 0.0);
        assertNotNull(account);
        String accNum = account.getAccountNumber();

        //Ensure account exists before closing
        assertTrue(accountService.findAccountForUser(accNum, testUser).isPresent());

        boolean success = accountService.closeAccount(accNum, testUser);
        assertTrue("Closing account with zero balance should succeed", success);

        //Ensure account no longer exists
        assertFalse(accountService.findAccountForUser(accNum, testUser).isPresent());
    }

    @Test
    public void testCloseAccountWithBalance() {
        BankAccount account = accountService.createAccount(testUser, AccountService.TYPE_CLIENT, "To Close", 50.0);
        assertNotNull(account);
        String accNum = account.getAccountNumber();

        boolean success = accountService.closeAccount(accNum, testUser);
        assertFalse("Closing account with positive balance should fail", success);

        //Ensure account still exists
        assertTrue(accountService.findAccountForUser(accNum, testUser).isPresent());
    }

    @Test
    public void testCloseAccountOverdrawn() {
        BankAccount account = accountService.createAccount(testUser, AccountService.TYPE_SMALL_BUSINESS, "To Close", 100.0);
        assertNotNull(account);
        String accNum = account.getAccountNumber();

        //Withdraw to use overdraft
        accountService.withdraw(accNum, bd("150.00"), testUser);

        boolean success = accountService.closeAccount(accNum, testUser);
        assertFalse("Closing overdrawn account should fail", success);

        assertTrue(accountService.findAccountForUser(accNum, testUser).isPresent());
    }
}

/*
 * Tests to be added:

 */


