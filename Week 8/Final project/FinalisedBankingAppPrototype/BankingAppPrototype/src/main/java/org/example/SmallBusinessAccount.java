package org.example;
import java.math.BigDecimal;

//Small Business account type REQ005,REQOO6
public class SmallBusinessAccount extends OverdraftManagement {

    //Overdraft limit for this account type

    private static final BigDecimal overdraftLimit = new BigDecimal("1000.00").setScale(CURRENCY_SCALE, CURRENCY_ROUNDING_MODE);

    public SmallBusinessAccount(String ownerUsername, String accountName, String accountNumber, double initialBalance) {
        super(ownerUsername, accountName, accountNumber, initialBalance);
    }
    @Override
    public BigDecimal getOverdraftFacility() {
        return overdraftLimit;
    }
    //This account type does not have signatory restriction.
    @Override
    public boolean requiresSignatoryAuth() {
        return false;
    }
}