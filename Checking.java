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
    public void deposit(double amount, boolean logTransaction) {
        setBalance(getBalance() + amount);
    
        // Log the deposit only if logTransaction is true
        if (logTransaction) {
            TransactionLog log = new TransactionLog();
            String logMessage = String.format("%s deposited $%.2f to Account %s",
                    getOwner().getName(), amount, getAccountNumber());
            log.logTransaction(logMessage);
            log.saveLog();
        }
    }

    /**
     * @param amount money to withdraw
     * @throws Exception amount larger than account balance
     * @Override
    */
    public void withdraw(double amount, boolean logTransaction) throws Exception {
        if (!(amount > 0 && (getBalance() + overdraftLimit) >= amount)) {
            throw new Exception("Insufficient balance for withdrawal.");
        }
        setBalance(getBalance() - amount);
    
        // Log the withdrawal only if logTransaction is true
        if (logTransaction) {
            TransactionLog log = new TransactionLog();
            String logMessage = String.format("%s withdrew $%.2f from Account %s",
                    getOwner().getName(), amount, getAccountNumber());
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
    public void transfer(Account toAccount, double amount) throws Exception {
        try {
            // Perform withdraw and deposit without logging
            withdraw(amount, false);  // Pass 'false' to indicate no logging
            toAccount.deposit(amount, false);
    
            // Log the transfer
            TransactionLog log = new TransactionLog();
            String logMessage = String.format("%s transferred $%.2f from Account %s to %s's Account %s",
                    getOwner().getName(), amount, getAccountNumber(), toAccount.getOwner().getName(), toAccount.getAccountNumber());
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
