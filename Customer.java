import java.util.ArrayList;

public class Customer extends Person{
    private int customerId;
    private ArrayList<Account> Account;

    public void addAccount(Account accountToAdd) {

    }

    public void removeAccount( Account accountToDelete) {

    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setAccount(ArrayList<Account> account) {
        Account = account;
    }

    public ArrayList<Account> getAccount() {
        return Account;
    }
}
