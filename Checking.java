public class Checking extends Account {
    private double overdraftLimit;

    public void withdraw(double amount) {

    }

    public void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }
}
