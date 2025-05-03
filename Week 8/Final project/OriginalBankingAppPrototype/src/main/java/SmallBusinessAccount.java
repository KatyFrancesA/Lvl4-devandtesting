public class SmallBusinessAccount extends BankAccount {
    private double overdraftFacility = 1000.0;

    public SmallBusinessAccount(String accountName, String accountNumber, double balance) {
        super(accountName, accountNumber, balance);
    }

    public double getOverdraftFacility() {
        return overdraftFacility;
    }

    @Override
    public void withdraw(double amount) {
        if (amount > getBalance() + overdraftFacility) {
            System.out.println("Insufficient funds!");
        } else {
            if (amount > getBalance()) {
                overdraftFacility -= (amount - getBalance());
                setBalance(0);
            } else {
                setBalance(getBalance() - amount);
            }
            System.out.println("Withdrawal successful!");
        }
    }

    private void setBalance(double d) {
    }
}
