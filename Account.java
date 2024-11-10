public abstract class Account {
    private String accountNumber;
    private double balance;
    private Customer owner;

    public Account(String accountNumber, Customer owner, double balance) {
        this.accountNumber = accountNumber;
        this.owner = owner;
        this.balance = balance;
    }

    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public Customer getOwner() { return owner; }

    public abstract void depositTransaction(double amount, boolean logTransaction);
    public abstract void withdrawTransaction(double amount, boolean logTransaction) throws Exception;
    public abstract void transferTransaction(Account toAccount, double amount) throws Exception;
    public abstract void inquireBalance();

    protected abstract boolean validateDeposit(double amount);
    protected abstract boolean validateWithdrawal(double amount);
}
