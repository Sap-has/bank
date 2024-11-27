package new_bank.Accounts;

import new_bank.TransactionLog;
import new_bank.operations.Customer;

/**
 * Creates a checking account
 * @author Epifanio Sarinana
 * @author Kayra Dominguez
 */
public class Checking extends Account {
    private double overdraftLimit;

    /**
     * 
     * @param accountNumber checking account number
     * @param owner Person who owns checking account
     * @param balance amount of money in checking account
     * @param overdraftLimit the overdraft limit of the checking account
     */
    public Checking(String accountNumber, Customer owner, double balance, double overdraftLimit) {
        super(accountNumber, owner, balance);
        this.overdraftLimit = overdraftLimit;
    }

    /**
     * 
     * @param overdraftLimit checking account's overdraft limit
     */
    public void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    /**
     * 
     * @return overdraftlimit of checking account
     */
    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    /**
     * @param amount money to deposit to checking account
     * @Override
     * */
    public void depositTransaction(double amount, boolean logTransaction) {
        setBalance(getBalance() + amount);
    
        // Log the deposit only if logTransaction is true
        if (logTransaction) {
            TransactionLog log = new TransactionLog();
            String logMessage = String.format("%s deposited $%.2f to Checking %s. New Balance: $%.2f",
                    getOwner().getName(), amount, getAccountNumber(), getBalance());
            log.logTransaction(logMessage);
            log.saveLog();
        }
    }

    /**
     * @param amount money to withdraw
     * @throws Exception amount larger than account balance
     * @Override
    */
    public void withdrawTransaction(double amount, boolean logTransaction) throws Exception {
        if (!(amount > 0 && (getBalance() + overdraftLimit) >= amount)) {
            throw new Exception("Insufficient balance for withdrawal.");
        }
        setBalance(getBalance() - amount);
    
        // Log the withdrawal only if logTransaction is true
        if (logTransaction) {
            TransactionLog log = new TransactionLog();
            String logMessage = String.format("%s withdrew $%.2f from Checking %s. New Balance: $%.2f",
                    getOwner().getName(), amount, getAccountNumber(), getBalance());
            log.logTransaction(logMessage);
            log.saveLog();
        }
    }
    

    /**
     * @param toAccount transfer money to someone else's account
     * @param amount money to transfer
     * @throws Exception 
     * @Override
     */
    public void transferTransaction(Account toAccount, double amount) throws Exception {
        try {
            // Perform withdraw and deposit without logging
            withdrawTransaction(amount, false);  // Pass 'false' to indicate no logging
            toAccount.depositTransaction(amount, false);
    
            // Log the transfer
            TransactionLog log = new TransactionLog();
            String logMessage = String.format("%s transferred $%.2f from Checking %s to %s's Account %s. Checking %s Balance: $%.2f. Account %s Balance: $%.2f.",
                    getOwner().getName(), amount, getAccountNumber(), toAccount.getOwner().getName(), toAccount.getAccountNumber(), getAccountNumber(), getBalance(), toAccount.getAccountNumber(), toAccount.getBalance());
            log.logTransaction(logMessage);
            log.saveLog();
        } catch (Exception e) {
            throw new Exception("Transfer failed: " + e.getMessage());
        }
    }
    

    /**
     * @Override
    */
    public void inquireBalance() {
        TransactionLog log = new TransactionLog();
        String logMessage = String.format("%s inquired balance of %s: $%.2f", getOwner().getName(), getAccountNumber(), getBalance());
        log.logTransaction(logMessage);
        log.saveLog();
    }
}
