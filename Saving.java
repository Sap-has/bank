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
        deposit(interest);
        TransactionLog log = new TransactionLog();
        log.logTransaction("Applied interest of " + interest + " to Saving Account " + getAccountNumber());
        log.saveLog();
    }

    /**
     * @param amount money to deposit 
     * @Override
     */
    public void deposit(double amount) {
        if (amount > 0) {
            setBalance(getBalance() + amount);
            TransactionLog log = new TransactionLog();
            log.logTransaction(getOwner().getName() + " deposited $" + amount + " to Saving Account " + getAccountNumber() + ". New Saving account balance: $" + getBalance());
            log.saveLog();
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    /**
     * @param amount money to withdraw
     * @throws Exception if withdraw amount exceeds savings balance 
     * @Override
     */
    public void withdraw(double amount) throws Exception {
        if (amount > 0 && getBalance() >= amount) {
            setBalance(getBalance() - amount);
            TransactionLog log = new TransactionLog();
            log.logTransaction(getOwner().getName() + " deposited $" + amount + " to Saving Account " + getAccountNumber() + ". New saving account balance: $" + getBalance());
            log.saveLog();
        } else {
            throw new Exception("Insufficient funds for withdrawal.");
        }
    }

    /**
     * @param toAccount account to transfer to
     * @param amount money to transfer
     * @throws Exception
     * @Override
     */
    public void transfer(Account toAccount, double amount) throws Exception {
        withdraw(amount);
        toAccount.deposit(amount);
        TransactionLog log = new TransactionLog();
        log.logTransaction(getOwner().getName() + " transferred " + amount + " from Checking Account " + getAccountNumber() + " to " + toAccount.getOwner().getName() + "'s to " + toAccount.getAccountNumber());
        log.saveLog();
    }

    /**
     * @Override
     */
    public String inquireBalance() {
        return "Balance in Saving Account " + getAccountNumber() + ": " + getBalance();
    }
}
