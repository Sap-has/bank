public class Checking extends Account {
    private double overdraftLimit;

    public Checking(String accountNumber, Person owner, double balance, double overdraftLimit) {
        super(accountNumber, owner, balance);
        this.overdraftLimit = overdraftLimit;
    }

    public void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    @Override
    public void deposit(double amount) {
        // TODO Deposit money into account and properly log the entry
    }

    @Override
    public void withdraw(double amount) throws Exception {
        // TODO Witthdraw money, log entry, throw error with message
    }

    @Override
    public void transfer(Account toAccount, double amount) throws Exception {
        // TODO Transfer moeny, log 2 entries, throw error with message
    }

    @Override
    public void inquireBalance() {
        // TODO print balance and log entry
       
    }
}
