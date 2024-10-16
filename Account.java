public abstract class Account {
    private String accountNumber;
    private double balance;
    private Person owner;

    public Account(String accountNumber, Person owner, double balance) {
        this.accountNumber = accountNumber;
        this.owner = owner;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public abstract void deposit(double amount);

    public abstract void withdraw(double amount) throws Exception;

    public abstract void transfer(Account toAccount, double amount) throws Exception;

    public abstract void inquireBalance();
    
}
