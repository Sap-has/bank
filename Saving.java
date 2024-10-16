public class Saving extends Account {
    private double interestRate;

    public Saving(String accountNumber, Person owner, double balance, double interestRate) {
        super(accountNumber, owner, balance);
        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public void applyInterest() {
        // TODO apply an interest, log entry
    }

    @Override
    public void deposit(double amount) {
        // TODO deposit
    }

    @Override
    public void withdraw(double amount) throws Exception {
        // TODO withdraw
    }

    @Override
    public void transfer(Account toAccount, double amount) throws Exception {
        // TODO transfer
    }

    @Override
    public void inquireBalance() {
        // TODO print balance
    }
}
