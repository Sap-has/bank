public class Credit extends Account {
    private double creditLimit;
    private double startingBalance;

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public void setStartingBalance(double startingBalance) {
        this.startingBalance = startingBalance;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public double getStartingBalance() {
        return startingBalance;
    }
}
