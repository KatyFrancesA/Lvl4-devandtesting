public class ClientAccount extends BankAccount {
    private double overdraftFacility = 1500.0;

    public ClientAccount(String accountName, String accountNumber, double balance) {
        super(accountName, accountNumber, balance);
    }

    public double getOverdraftFacility() {
        return overdraftFacility;
    }
}
