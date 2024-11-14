/**
 * The Account class represents a bank account with basic operations such as deposits, withdrawals, 
 * and transfers. It is an abstract class that defines common behaviors and requires subclasses to 
 * implement specific transaction logic.
 */
public abstract class Account {
    
    /**
     * The account number of this account.
     */
    private String accountNumber;

    /**
     * The current balance of this account.
     */
    private double balance;

    /**
     * The customer who owns this account.
     */
    private Customer owner;

    /**
     * Constructs an Account with the specified account number, owner, and initial balance.
     * 
     * @param accountNumber the unique identifier for the account
     * @param owner the customer who owns the account
     * @param balance the initial balance of the account
     */
    public Account(String accountNumber, Customer owner, double balance) {
        this.accountNumber = accountNumber;
        this.owner = owner;
        this.balance = balance;
    }

    /**
     * Returns the account number of this account.
     * 
     * @return the account number
     */
    public String getAccountNumber() { 
        return accountNumber; 
    }

    /**
     * Returns the current balance of this account.
     * 
     * @return the current balance
     */
    public double getBalance() { 
        return balance; 
    }

    /**
     * Sets the balance of this account to the specified amount.
     * 
     * @param balance the new balance to set
     */
    public void setBalance(double balance) { 
        this.balance = balance; 
    }

    /**
     * Returns the owner of this account.
     * 
     * @return the customer who owns the account
     */
    public Customer getOwner() { 
        return owner; 
    }

    /**
     * Performs a deposit transaction, adding the specified amount to the account balance.
     * 
     * @param amount the amount to deposit
     * @param logTransaction a flag indicating whether to log the transaction
     */
    public abstract void depositTransaction(double amount, boolean logTransaction);

    /**
     * Performs a withdrawal transaction, subtracting the specified amount from the account balance.
     * 
     * @param amount the amount to withdraw
     * @param logTransaction a flag indicating whether to log the transaction
     * @throws Exception if the withdrawal cannot be processed (e.g., insufficient funds)
     */
    public abstract void withdrawTransaction(double amount, boolean logTransaction) throws Exception;

    /**
     * Performs a transfer transaction, moving the specified amount to another account.
     * 
     * @param toAccount the target account to transfer to
     * @param amount the amount to transfer
     * @throws Exception if the transfer cannot be completed (e.g., insufficient funds)
     */
    public abstract void transferTransaction(Account toAccount, double amount) throws Exception;

    /**
     * Displays the current balance of the account.
     */
    public abstract void inquireBalance();
}
