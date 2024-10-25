public class Saving extends Account {
    private double interestRate;

    public Saving(String accountNumber, Customer owner, double balance, double interestRate) {
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
        double interest = getBalance() * interestRate;
        deposit(interest);
        TransactionLog log = new TransactionLog();
        log.logTransaction("Applied interest of " + interest + " to Saving Account " + getAccountNumber());
        log.saveLog();
    }

    @Override
    public void deposit(double amount) {
        if (amount > 0) {
            setBalance(getBalance() + amount);
            TransactionLog log = new TransactionLog();
            log.logTransaction("Deposited " + amount + " to Saving Account " + getAccountNumber());
            log.saveLog();
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    @Override
    public void withdraw(double amount) throws Exception {
        if (amount > 0 && getBalance() >= amount) {
            setBalance(getBalance() - amount);
            TransactionLog log = new TransactionLog();
            log.logTransaction("Withdrew " + amount + " from Saving Account " + getAccountNumber());
            log.saveLog();
        } else {
            throw new Exception("Insufficient funds for withdrawal.");
        }
    }

    @Override
    public void transfer(Account toAccount, double amount) throws Exception {
        withdraw(amount);
        toAccount.deposit(amount);
        TransactionLog log = new TransactionLog();
        log.logTransaction("Transferred " + amount + " from Saving Account " + getAccountNumber() + " to " + toAccount.getAccountNumber());
        log.saveLog();
    }

    @Override
    public String inquireBalance() {
        return "Balance in Saving Account " + getAccountNumber() + ": " + getBalance();
    }
}
