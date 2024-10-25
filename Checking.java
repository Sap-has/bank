public class Checking extends Account {
    private double overdraftLimit;

    public Checking(String accountNumber, Customer owner, double balance, double overdraftLimit) {
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
        if (amount > 0) {
            setBalance(getBalance() + amount);
            TransactionLog log = new TransactionLog();
            log.logTransaction("Deposited " + amount + " to Checking Account " + getAccountNumber());
            log.saveLog();
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    @Override
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

    @Override
    public void transfer(Account toAccount, double amount) throws Exception {
        withdraw(amount);
        toAccount.deposit(amount);
        TransactionLog log = new TransactionLog();
        log.logTransaction("Transferred " + amount + " from Checking Account " + getAccountNumber() + " to " + toAccount.getAccountNumber());
        log.saveLog();
    }

    @Override
    public String inquireBalance() {
        return "Balance in Checking Account " + getAccountNumber() + ": " + getBalance() ;
    }
}
