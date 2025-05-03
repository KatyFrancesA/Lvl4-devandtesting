package org.example;
import java.math.BigDecimal;

//Community account type REQ005, REQ006, REQ004
public class CommunityAccount extends OverdraftManagement {

    //Overdraft limit for this account type
     
    private static final BigDecimal overdraftLimit = new BigDecimal("2500.00").setScale(CURRENCY_SCALE, CURRENCY_ROUNDING_MODE);
    public CommunityAccount(String ownerUsername, String accountName, String accountNumber, double initialBalance) {
        super(ownerUsername, accountName, accountNumber, initialBalance);
    }

    @Override
    public BigDecimal getOverdraftFacility() {
        return overdraftLimit;
    }
    //REQ004 signatory restriction on this account type
    @Override
    public boolean requiresSignatoryAuth() {
        return true;
    }
}