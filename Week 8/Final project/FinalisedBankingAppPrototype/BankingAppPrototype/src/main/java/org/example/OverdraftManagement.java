package org.example;
import java.math.BigDecimal;

/*
* Reworked overdraft logic and moved into separte class for better readability and code updates in future
* It handles tracking overdraft usage, ensuring overdraft limits are taken into account for withdrawals, deposit, transfers etc
 */
//Overdraft logic
public abstract class OverdraftManagement extends BankAccount {
    //Tracks overdraft usage
    protected BigDecimal currentOverdraftUsed;

    public OverdraftManagement(String ownerUsername, String accountName, String accountNumber, double initialBalance) {
        //setting initial balance to 0 in super constructor, handle deposit here
        super(ownerUsername, accountName, accountNumber, 0.0);
         
        this.currentOverdraftUsed = BigDecimal.ZERO.setScale(CURRENCY_SCALE, CURRENCY_ROUNDING_MODE);

        if (initialBalance > 0.0) {
             
            BigDecimal initialAmount = BigDecimal.valueOf(initialBalance).setScale(CURRENCY_SCALE, CURRENCY_ROUNDING_MODE);
            deposit(initialAmount);
        }
    }

    /* REQ005
    * Fixed BUG002 and BUG003 by fixing the overdraft logic
    * Overrides the withdraw method in order to implement overdraft functionality
     */
    @Override
    public boolean withdraw(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Invalid withdrawal amount.");
            return false;
        }
         
        BigDecimal scaledAmount = amount.setScale(CURRENCY_SCALE, CURRENCY_ROUNDING_MODE);

        //Calculate total funds available (current balance + remaining overdraft)
        BigDecimal overdraftLimit = getOverdraftFacility();
        BigDecimal overdraftAvailable = overdraftLimit.subtract(currentOverdraftUsed);
        BigDecimal totalAvailableFunds = super.getBalance().add(overdraftAvailable); //Refactored so overdraft is added to balance

        //Check if withdrawal amount exceeds total funds
        if (scaledAmount.compareTo(totalAvailableFunds) <= 0) {
            //Determine if overdraft is needed
            if (scaledAmount.compareTo(super.getBalance()) <= 0) {
                //Standard withdrawal (enough positive balance)
                super.withdraw(scaledAmount);
            } else {
                //Overdraft needed
                BigDecimal overdraftNeeded = scaledAmount.subtract(super.getBalance());
                super.setBalance(BigDecimal.ZERO.setScale(CURRENCY_SCALE, CURRENCY_ROUNDING_MODE));
                this.currentOverdraftUsed = this.currentOverdraftUsed.add(overdraftNeeded).setScale(CURRENCY_SCALE, CURRENCY_ROUNDING_MODE);
            }
            return true; //Withdrawal successful
        } else {
            System.out.println("Insufficient funds.");
            System.out.println(getFormattedBalance());
            return false; //Insufficient funds
        }
    }

    @Override
    public void deposit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }
        BigDecimal scaledAmount = amount.setScale(CURRENCY_SCALE, CURRENCY_ROUNDING_MODE);

        //If overdrawn deposit repays overdraft first
        if (this.currentOverdraftUsed.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal repayment = scaledAmount.min(this.currentOverdraftUsed); //Amount to repay is the smaller of deposit or overdraft used
             
            this.currentOverdraftUsed = this.currentOverdraftUsed.subtract(repayment).setScale(CURRENCY_SCALE, CURRENCY_ROUNDING_MODE); //Reduce overdraft used
            scaledAmount = scaledAmount.subtract(repayment); //Remaining amount after overdraft repayment
        }
        //If there's any amount left after repaying overdraft adds to main balance
        if (scaledAmount.compareTo(BigDecimal.ZERO) > 0) {
            super.deposit(scaledAmount);
        }
    }
    @Override
    public String getFormattedBalance() {
        BigDecimal overdraftLimit = getOverdraftFacility(); //Overdraft limit
         
        BigDecimal remainingOverdraft = overdraftLimit.subtract(currentOverdraftUsed).setScale(CURRENCY_SCALE, CURRENCY_ROUNDING_MODE);
        String balanceStr;

        if (currentOverdraftUsed.compareTo(BigDecimal.ZERO) > 0) {
            //Display amount as negative if overdrawn (shows how much is owed)
            balanceStr = String.format("Balance: %s (Overdrawn)", currencyFormatter.format(currentOverdraftUsed.negate()));
        } else {
            //Show normal positive balance from superclass
            balanceStr = String.format("Balance: %s", currencyFormatter.format(super.getBalance()));
        }
        //Combine balance line with overdraft remaining
        return String.format("%s\nOverdraft Remaining: %s",
                balanceStr,
                currencyFormatter.format(remainingOverdraft));
    }
    @Override
    public BigDecimal getCurrentOverdraftUsed() {
        return this.currentOverdraftUsed.setScale(CURRENCY_SCALE, CURRENCY_ROUNDING_MODE);
    }
}