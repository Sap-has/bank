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
        // TODO implement deposit, pay back principle
    }

    @Override
    public void withdraw(double amount) throws Exception {
        // TODO withdraw, increase principle and don't exceed credit limit
    }

    @Override
    public void transfer(Account toAccount, double amount) throws Exception {
        // TODO transfer money, don't exceed credit limit, increase principle
    }

    @Override
    public void inquireBalance() {
        // TODO can you get a balance from a credit card?
        
    }
}
