/**
 * Creates a credit account
 * @author Epifanio Sarinana
 * @author Kayra Dominguez
 */
public class Credit extends Account {
    private double creditLimit;
    private double principle;

    /**
     * 
     * @param accountNumber credit account number
     * @param owner person who owns credit account
     * @param balance credit account balance
     * @param creditLimit the credit account's credit limit
     * @param principle the amount the person owes 
     */
    public Credit(String accountNumber, Customer owner, double balance, double creditLimit, double principle) {
        super(accountNumber, owner, balance);
        this.creditLimit = creditLimit;
        this.principle = principle;
    }

    /**
     * 
     * @return credit account limit
     */
    public double getCreditLimit() {
        return creditLimit;
    }

    /**
     * 
     * @param creditLimit set credit account limit
     */
    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    /**
     * 
     * @return credit principle
     */
    public double getPrinciple() {
        return principle;
    }

    /**
     * 
     * @param principle set credit account principle
     */
    public void setPrinciple(double principle) {
        this.principle = principle;
    }

    /**
     * @param amount credit amount to deposit toward principle
     * @Override
     */
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
     * @param amount money to withdraw from credit account 
     * @throws Exception if more money than credit limit is withdrawn
     * @Override
     */
    public void withdraw(double amount, boolean logTransaction) throws Exception {
        if (!(amount > 0 && Math.abs(getBalance()) >= amount && (creditLimit - Math.abs(getBalance())) >= amount)) {
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
     * @param toAccount account to transfer money to
     * @param amount amount of money to transfer
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
