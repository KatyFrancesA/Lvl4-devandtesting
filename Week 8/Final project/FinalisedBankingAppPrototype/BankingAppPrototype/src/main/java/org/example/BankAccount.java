package org.example;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

//Manages account types REQ004, REQ007
public abstract class BankAccount {
    private String ownerUsername;
    private String accountName;
    private final String accountNumber;

    protected BigDecimal balance;

    //Currency formatting for UK
    public static final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.UK);

    public static final int CURRENCY_SCALE = 2;
    public static final RoundingMode CURRENCY_ROUNDING_MODE = RoundingMode.HALF_UP;

    public BankAccount(String ownerUsername, String accountName, String accountNumber, double initialBalance) {
        this.ownerUsername = ownerUsername.toLowerCase();
        this.accountName = accountName;
        this.accountNumber = accountNumber;

        BigDecimal initialAmount = BigDecimal.valueOf(initialBalance).setScale(CURRENCY_SCALE, CURRENCY_ROUNDING_MODE);
        this.balance = initialAmount.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO.setScale(CURRENCY_SCALE, CURRENCY_ROUNDING_MODE) : initialAmount;
    }

    //Allow other classes read access to private data
    public String getOwnerUsername() { return ownerUsername; }
    public String getAccountName() { return accountName; }
    public String getAccountNumber() { return accountNumber; }
    public BigDecimal getBalance() {
         
        return balance.setScale(CURRENCY_SCALE, CURRENCY_ROUNDING_MODE);
    }

    //Allows AccountService class to update account details if username is changed
    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername.toLowerCase();
    }

    protected void setBalance(BigDecimal newBalance) { //Update private balance field

        this.balance = newBalance.setScale(CURRENCY_SCALE, CURRENCY_ROUNDING_MODE);
    }

    //Banking methods
    //Deposit
    public void deposit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Invalid input.");
            return;
        }
         
        BigDecimal scaledAmount = amount.setScale(CURRENCY_SCALE, CURRENCY_ROUNDING_MODE);
        setBalance(this.balance.add(scaledAmount));
    }
    //Withdraw
    public boolean withdraw(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Invalid input.");
            return false;
        }
         
        BigDecimal scaledAmount = amount.setScale(CURRENCY_SCALE, CURRENCY_ROUNDING_MODE);
        if (this.balance.compareTo(scaledAmount) >= 0) {
            setBalance(this.balance.subtract(scaledAmount));
            return true;
        } else {
            System.out.println("Insufficient funds.");
            return false;
        }
    }

    /*
    * Refactored to fix BUG007 ensuring the method returns true on successful transfers
     */
    //Transfer
    public boolean transferTo(BankAccount targetAccount, BigDecimal amount) {
        if (targetAccount == null) {
            return false;
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Invalid input.");
            return false;
        }
        if (this.withdraw(amount)) {
            targetAccount.deposit(amount);
            return true; //Transfer successful
        } else {
            System.out.println("Insufficient funds.");
            return false;
        }
    }
    //Overdraft and Signatory restriction
    public abstract BigDecimal getOverdraftFacility();
    public abstract BigDecimal getCurrentOverdraftUsed();
    public boolean requiresSignatoryAuth() {
        return false;
    }
    //Balance formatting
    public String getFormattedBalance() {
        return String.format("\nBalance: %s", currencyFormatter.format(this.balance));
    }
    @Override
    public String toString() {
        return String.format("BankAccount [Name: %s, Username: %s, Account Number: %s, Balance: %s]",
                accountName, ownerUsername, accountNumber, currencyFormatter.format(this.balance.setScale(CURRENCY_SCALE, CURRENCY_ROUNDING_MODE)));
    }
}