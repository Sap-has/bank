public class Credit extends Account {
    private double creditLimit;

    /**
     * 
     * @param accountNumber credit account number
     * @param owner person who owns credit account
     * @param balance credit account balance
     * @param creditLimit the credit account's credit limit
     * @param principle the amount the person owes 
     */
    public Credit(String accountNumber, Customer owner, double balance) {
        super(accountNumber, owner, balance);
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
     * @param amount credit amount to deposit toward principle
     * @Override
     */
    public void depositTransaction(double amount, boolean logTransaction) {
        setBalance(getBalance() + amount);
    
        // Log the deposit only if logTransaction is true
        if (logTransaction) {
            TransactionLog log = new TransactionLog();
            String logMessage = String.format("%s deposited $%.2f to Credit %s. New Balance: $%.2f",
                    getOwner().getName(), amount, getAccountNumber(), getBalance());
            log.logTransaction(logMessage);
            log.saveLog();
        }
    }

    /**
     * @param amount money to withdraw from credit account 
     * @throws Exception if more money than credit limit is withdrawn
     * @Override
     */
    public void withdrawTransaction(double amount, boolean logTransaction) throws Exception {
        if (Math.abs(getBalance()) < amount && (creditLimit - Math.abs(getBalance())) < amount) {
            throw new insufficientFundsException();
        }
        setBalance(getBalance() - amount);
    
        // Log the withdrawal only if logTransaction is true
        if (logTransaction) {
            TransactionLog log = new TransactionLog();
            String logMessage = String.format("%s withdrew $%.2f from Credit %s. New Balance: $%.2f",
                    getOwner().getName(), amount, getAccountNumber(), getBalance());
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
    public void transferTransaction(Account toAccount, double amount) throws Exception {
        try {
            // Perform withdraw and deposit without logging
            withdrawTransaction(amount, false);  // Pass 'false' to indicate no logging
            toAccount.depositTransaction(amount, false);
    
            // Log the transfer
            TransactionLog log = new TransactionLog();
            String logMessage = String.format("%s transferred $%.2f from Credit %s to %s's Account %s. Credit %s Balance: $%.2f. Account %s Balance: $%.2f.",
                    getOwner().getName(), amount, getAccountNumber(), toAccount.getOwner().getName(), toAccount.getAccountNumber(), getAccountNumber(), getBalance(), toAccount.getAccountNumber(), toAccount.getBalance());
            log.logTransaction(logMessage);
            log.saveLog();
        } catch (Exception e) {
            throw new Exception("Transfer failed: " + e.getMessage());
        }
    }
}