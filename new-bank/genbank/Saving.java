package genbank;

public class Saving extends Account {
    /**
     * 
     * @param accountNumber saving account number
     * @param owner person who owns saving account
     * @param balance saving account balance
     * @param interestRate saving account interest rate
     */
    public Saving(String accountNumber, Customer owner, double balance) {
        super(accountNumber, owner, balance);
    }

    /**
     * @param amount money to deposit 
     * @Override
     */
    public void depositTransaction(double amount, boolean logTransaction) {
        setBalance(getBalance() + amount);
    
        // Log the deposit only if logTransaction is true
        if (logTransaction) {
            TransactionLog log = new TransactionLog();
            String logMessage = String.format("%s deposited $%.2f to Saving %s. New Balance: $%.2f",
                    getOwner().getName(), amount, getAccountNumber(), getBalance());
            log.logTransaction(logMessage);
            log.saveLog();
        }
    }

    /**
     * @param amount money to withdraw
     * @throws Exception if withdraw amount exceeds savings balance 
     * @Override
     */
    public void withdrawTransaction(double amount, boolean logTransaction) throws Exception {
        if (!(amount > 0 && getBalance() >= amount)) {
            throw new insufficientFundsException();
        }
        setBalance(getBalance() - amount);
    
        // Log the withdrawal only if logTransaction is true
        if (logTransaction) {
            TransactionLog log = new TransactionLog();
            String logMessage = String.format("%s withdrew $%.2f from Saving %s. New Balance: $%.2f",
                    getOwner().getName(), amount, getAccountNumber(), getBalance());
            log.logTransaction(logMessage);
            log.saveLog();
        }
    }

    /**
     * @param toAccount account to transfer to
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
            String logMessage = String.format("%s transferred $%.2f from Saving %s to %s's Account %s. Account %s Balance: $%.2f. Account %s Balance: $%.2f.",
                    getOwner().getName(), amount, getAccountNumber(), toAccount.getOwner().getName(), toAccount.getAccountNumber(), getAccountNumber(), getBalance(), toAccount.getAccountNumber(), toAccount.getBalance());
            log.logTransaction(logMessage);
            log.saveLog();
        } catch (Exception e) {
            throw new Exception("Transfer failed: " + e.getMessage());
        }
    }
}