/**
 * Create saving account
 * @author Epifanio Sarinana
 * @author Kayra Dominguez
 */
public class Saving extends Account {
    private double interestRate;

    /**
     * 
     * @param accountNumber saving account number
     * @param owner person who owns saving account
     * @param balance saving account balance
     * @param interestRate saving account interest rate
     */
    public Saving(String accountNumber, Customer owner, double balance, double interestRate) {
        super(accountNumber, owner, balance);
        this.interestRate = interestRate;
    }

    /**
     * 
     * @return saving account interest rate
     */
    public double getInterestRate() {
        return interestRate;
    }

    /**
     * 
     * @param interestRate interest rate to give saving account
     */
    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }


    public void applyInterest() {
        double interest = getBalance() * interestRate;
        depositTransaction(interest, true);
        TransactionLog log = new TransactionLog();
        log.logTransaction("Applied interest of " + interest + " to Saving Account " + getAccountNumber());
        log.saveLog();
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
            String logMessage = String.format("%s deposited $%.2f to Account %s. New Balance: $%.2f",
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
            throw new Exception("Insufficient balance for withdrawal.");
        }
        setBalance(getBalance() - amount);
    
        // Log the withdrawal only if logTransaction is true
        if (logTransaction) {
            TransactionLog log = new TransactionLog();
            String logMessage = String.format("%s withdrew $%.2f from Account %s. New Balance: $%.2f",
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
            String logMessage = String.format("%s transferred $%.2f from Account %s to %s's Account %s. Account %s Balance: $%.2f. Account %s Balance: $%.2f.",
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

    @Override
    protected boolean validateDeposit(double amount) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validateDeposit'");
    }

    @Override
    protected boolean validateWithdrawal(double amount) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validateWithdrawal'");
    }
}
