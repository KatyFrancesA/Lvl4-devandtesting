package org.example;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
*This class centralises all account related logic and replaces the account management logic
that was spread across the Main class
 */
//Manages bank accounts
public class AccountService {
    private final List<BankAccount> accounts; //Store accounts in a list
    private final UserService userService; //Stored users

    //Counter for generating account numbers starting from 1
    private static int nextAccountNumber = 1;

    //Reset static acc number counter for AccountServiceTest
    public static void resetNextAccountNumberForTest() {
        nextAccountNumber = 1;
    }

    public static final String TYPE_SMALL_BUSINESS = "smallbusiness";
    public static final String TYPE_COMMUNITY = "community";
    public static final String TYPE_CLIENT = "client";

    public AccountService(UserService userService) {
        this.accounts = new ArrayList<>();
        this.userService = userService;
    }

    /* REQ002, REQ006
    * Handles bank account creation and of specific types and prevents the creation of duplicate account types
    * Based on the accountType parameter
    * Fixed BUG001 by using BigDecimal instead of double to format my currency input which fixed the initial balance bug
     */
    public BankAccount createAccount(String ownerUsername, String accountType, String accountName, double initialBalance) {

        //Counts up to generate account number after first has been assigned and so on
        String accountNumber = String.valueOf(nextAccountNumber++);
        BankAccount account = null;

        /*
        * Create account type
        * BUG001 fix, initial balance set to 0 on each account type
         */
        switch (accountType.toLowerCase()) {
            case TYPE_SMALL_BUSINESS:
                account = new SmallBusinessAccount(ownerUsername, accountName, accountNumber, initialBalance);
                break;
            case TYPE_COMMUNITY:
                account = new CommunityAccount(ownerUsername, accountName, accountNumber, initialBalance);
                break;
            case TYPE_CLIENT:
                account = new ClientAccount(ownerUsername, accountName, accountNumber, initialBalance);
                break;
            default:
                System.out.println("Invalid input");
                nextAccountNumber--;
                return null;
        }
        //Add new account to list
        accounts.add(account);
        return account;
    }

    public boolean accountTypeExistsForUser(String username, String accountType) {
        String lowerUsername = username.toLowerCase();
        String lowerAccountType = accountType.toLowerCase();

        return accounts.stream() //Tracks what account types already owned by user
                .filter(acc -> acc.getOwnerUsername().equals(lowerUsername))
                .anyMatch(acc -> {
                    if (lowerAccountType.equals(TYPE_SMALL_BUSINESS) && acc instanceof SmallBusinessAccount) return true;
                    if (lowerAccountType.equals(TYPE_COMMUNITY) && acc instanceof CommunityAccount) return true;
                    if (lowerAccountType.equals(TYPE_CLIENT) && acc instanceof ClientAccount) return true;
                    return false;
                });
    }
    //Finds account by number and owner
    public Optional<BankAccount> findAccountForUser(String accountNumber, String username) {
        String lowerUsername = username.toLowerCase();
        return accounts.stream()
                .filter(acc -> acc.getAccountNumber().equals(accountNumber) && acc.getOwnerUsername().equals(lowerUsername))
                .findFirst();
    }
    //Returns users bank accounts
    public List<BankAccount> getAccountsForUser(String username) {
        String lowerUsername = username.toLowerCase();
        return accounts.stream()
                .filter(acc -> acc.getOwnerUsername().equals(lowerUsername))
                .collect(Collectors.toList());
    }
    //Banking operation methods
    public boolean deposit(String accountNumber, BigDecimal amount, String username) {
        Optional<BankAccount> accountOpt = findAccountForUser(accountNumber, username);
        if (accountOpt.isPresent()) {
            accountOpt.get().deposit(amount);
            return true;
        } else {
            System.out.println("Deposit failed. \nAccount not found or not owned by you.");
            return false;
        }
    }

    //REQ007
    public boolean withdraw(String accountNumber, BigDecimal amount, String username) {
        Optional<BankAccount> accountOpt = findAccountForUser(accountNumber, username);
        if (accountOpt.isPresent()) {
            BankAccount account = accountOpt.get();
            //REQ004 signatory check done before allowing withdraw (handled in Menu)

            //Return false if not enough funds including overdraft
            boolean success = account.withdraw(amount);
            return success;
        } else {
            System.out.println("Withdrawal failed. \nAccount not found or not owned by you.");
            return false;
        }
    }

    //REQ007 transfer using BigDecimal instead of double for currency formatting
    public boolean transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount, String username) {

        Optional<BankAccount> fromAccountOpt = findAccountForUser(fromAccountNumber, username);
        Optional<BankAccount> toAccountOpt = findAccountForUser(toAccountNumber, username);

        if (fromAccountOpt.isPresent() && toAccountOpt.isPresent()) {
            BankAccount fromAccount = fromAccountOpt.get();
            BankAccount toAccount = toAccountOpt.get();
            //REQ004 (Signatory check handled in Menu before calling transfer)

            //transferTo calls withdraw method checking balance/overdraft and then deposit
            boolean success = fromAccount.transferTo(toAccount, amount);
            return success;
        } else {
            //Print error for which account failed
            if (fromAccountOpt.isEmpty()) {
                System.out.println("Transfer failed. Account " + fromAccountNumber + " not found or not owned by you.");
            }
            if (toAccountOpt.isEmpty()) {
                System.out.println("Transfer failed. Account " + toAccountNumber + " not found or not owned by you.");
            }
            return false;
        }
    }
    //REQ007
    public String checkBalance(String accountNumber, String username) {
        Optional<BankAccount> accountOpt = findAccountForUser(accountNumber, username);
        return accountOpt
                .map(BankAccount::getFormattedBalance)
                .orElse("Account " + accountNumber + " not found or not owned by " + username + ".");
    }
    //REQ001, REQ004
    public boolean closeAccount(String accountNumber, String username) {

        Optional<BankAccount> accountOpt = findAccountForUser(accountNumber, username);
        if (accountOpt.isPresent()) {
            BankAccount account = accountOpt.get();

            //Account must be empty with no overdraft used before allowing closure
            BigDecimal currentBalance = account.getBalance();
            BigDecimal overdraftUsed = account.getCurrentOverdraftUsed();

            if (currentBalance.compareTo(BigDecimal.ZERO) != 0 || overdraftUsed.compareTo(BigDecimal.ZERO) != 0) {
                if (currentBalance.compareTo(BigDecimal.ZERO) != 0) {
                    System.out.println("Account cannot be closed. Balance must be zero.");
                }
                if (overdraftUsed.compareTo(BigDecimal.ZERO) != 0) {
                    System.out.println("Account cannot be closed while overdrawn.");
                }
                System.out.println(account.getFormattedBalance());
                return false; //Prevent closure if not settled
            }
            //If reqs are met remove account from the list
            accounts.remove(account);
            System.out.println("Account " + accountNumber + " owned by " + username + " has been closed.\n\nReturning to Account Settings...");
            return true;
        } else {
            System.out.println("Account closure failed. Account not found or not owned by you.");
            return false;
        }
    }
    //Keep account ownership synced with username changes
    public void updateOwnerUsername(String oldUsername, String newUsername) {
        for (BankAccount account : accounts) {
            if (account.getOwnerUsername().equals(oldUsername)) {
                account.setOwnerUsername(newUsername);
            }
        }
    }
}