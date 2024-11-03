/**
 * Used to create a bank account for a person
 * @author Epifanio Sarinana
 * @author Kayra Dominguez
 */
public abstract class Account {
    private String accountNumber;
    private double balance;
    private Customer owner;

    /**
     * @param accountNumber bank account number
     * @param owner person who owns bank account
     * @param balance balance in the bank account
     */
    public Account(String accountNumber, Customer owner, double balance) {
        this.accountNumber = accountNumber;
        this.owner = owner;
        this.balance = balance;
    }

    /**
     * @return bank account number
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * @param accountNumber 
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * @return account balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * @param balance balance of the account 
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * @return Person that owns the bank account
     */
    public Customer getOwner() {
        return owner;
    }

    /**
     * @param owner the Person who owns the bank account
     */
    public void setOwner(Customer owner) {
        this.owner = owner;
    }

    public abstract void deposit(double amount, boolean logTransaction);

    public abstract void withdraw(double amount, boolean logTransaction) throws Exception;

    public abstract void transfer(Account toAccount, double amount) throws Exception;

    public abstract void inquireBalance();
}
