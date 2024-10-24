/**
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
    public Checking(String accountNumber, Person owner, double balance, double overdraftLimit) {
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
    public void deposit(double amount) {
        if (amount > 0) {
            setBalance(getBalance() + amount);
            TransactionLog log = new TransactionLog();
            log.logTransaction("Deposited " + amount + " to Checking Account " + getAccountNumber());
            log.saveLog();
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    /**
     * @param amount money to withdraw
     * @throws Exception amount larger than account balance
     * @Override
    */
    public void withdraw(double amount) throws Exception {
        if (amount > 0 && (getBalance() + overdraftLimit) >= amount) {
            setBalance(getBalance() - amount);
            TransactionLog log = new TransactionLog();
            log.logTransaction("Withdrew " + amount + " from Checking Account " + getAccountNumber());
            log.saveLog();
        } else {
            throw new Exception("Insufficient funds for withdrawal.");
        }
    }

    /**
     * @param toAccount transfer money to someone else's account
     * @param amount money to transfer
     * @throws Exception 
     * @Override
     */
    public void transfer(Account toAccount, double amount) throws Exception {
        withdraw(amount);
        toAccount.deposit(amount);
        TransactionLog log = new TransactionLog();
        log.logTransaction("Transferred " + amount + " from Checking Account " + getAccountNumber() + " to " + toAccount.getAccountNumber());
        log.saveLog();
    }

    /**
     * @Override
    */
    public void inquireBalance() {
        System.out.println("Balance in Checking Account " + getAccountNumber() + ": " + getBalance());
    }
}
