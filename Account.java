public abstract class Account {
    private int accountNumber;
    private double balance;
    private Person owner;

    public Account() {
        
    }

    public void inquireBalance() {

    }

    public void deposit(double amount) {

    }

    public void withdraw(double amount) {

    }

    public void transfer(Account toAccount, double amount) {

    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public Person getOwner() {
        return owner;
    }


}