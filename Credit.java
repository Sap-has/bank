public class Credit extends Account {
    private double creditLimit;
    private double principle;

    public Credit(String accountNumber, Person owner, double balance, double creditLimit, double principle) {
        super(accountNumber, owner, balance);
        this.creditLimit = creditLimit;
        this.principle = principle;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public double getPrinciple() {
        return principle;
    }

    public void setPrinciple(double principle) {
        this.principle = principle;
    }

    @Override
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

    @Override
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

    @Override
    public void transfer(Account toAccount, double amount) throws Exception {
        withdraw(amount);
        toAccount.deposit(amount);
        TransactionLog log = new TransactionLog();
        log.logTransaction("Transferred " + amount + " from Credit Account " + getAccountNumber() + " to " + toAccount.getAccountNumber());
        log.saveLog();
    }

    @Override
    public void inquireBalance() {
        System.out.println("Balance in Credit Account " + getAccountNumber() + ": " + getBalance() + " (Principle: " + principle + ")");
    }
}
