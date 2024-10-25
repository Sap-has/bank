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
    public Credit(String accountNumber, Person owner, double balance, double creditLimit, double principle) {
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
    public void deposit(double amount) {
        if (amount > 0) {
            setBalance(getBalance() + amount);
            principle -= amount; // pay back principle
            TransactionLog log = new TransactionLog();
            log.logTransaction("Deposited " + amount + " to Credit Account " + getAccountNumber());
            log.saveLog();
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    /**
     * @param amount money to withdraw from credit account 
     * @throws Exception if more money than credit limit is withdrawn
     * @Override
     */
    public void withdraw(double amount) throws Exception {
        if (amount > 0 && (getBalance() + (creditLimit - principle)) >= amount) {
            principle += amount;
            setBalance(getBalance() - amount);
            TransactionLog log = new TransactionLog();
            log.logTransaction("Withdrew " + amount + " from Credit Account " + getAccountNumber());
            log.saveLog();
        } else {
            throw new Exception("Credit limit exceeded for withdrawal.");
        }
    }

    /**
     * @param toAccount account to transfer money to
     * @param amount amount of money to transfer
     * @throws Exception
     * @Override
     */
    public void transfer(Account toAccount, double amount) throws Exception {
        withdraw(amount);
        toAccount.deposit(amount);
        TransactionLog log = new TransactionLog();
        log.logTransaction("Transferred " + amount + " from Credit Account " + getAccountNumber() + " to " + toAccount.getAccountNumber());
        log.saveLog();
    }

    /**
     * @Override
     */
    public void inquireBalance() {
        System.out.println("Balance in Credit Account " + getAccountNumber() + ": " + getBalance() + " (Principle: " + principle + ")");
    }
}
