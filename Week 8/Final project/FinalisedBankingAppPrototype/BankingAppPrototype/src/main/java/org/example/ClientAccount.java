package org.example;
import java.math.BigDecimal;

//Client account type REQ005, REQ006
public class ClientAccount extends OverdraftManagement {

    //overdraft limit for account type
    private static final BigDecimal overdraftLimit = new BigDecimal("1500.00").setScale(CURRENCY_SCALE, CURRENCY_ROUNDING_MODE);

    public ClientAccount(String ownerUsername, String accountName, String accountNumber, double initialBalance) {
        super(ownerUsername, accountName, accountNumber, initialBalance);
    }

    @Override
    public BigDecimal getOverdraftFacility() {
        return overdraftLimit;
    }
    //This account type does not have signatory restriction
    @Override
    public boolean requiresSignatoryAuth() {
        return false;
    }
}