import java.util.ArrayList;

public class Customer extends Person {
    private ArrayList<Account> accounts;
    private int customerID;

    public Customer(String name, String address, int customerID) {
        super(name, address);
        this.accounts = new ArrayList<>();
        this.customerID = customerID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void addAccount(Account account) {
        accounts.add(account); // removes first occurrence of the element of account
    }

    public void removeAccount(Account account) {
        accounts.remove(account); // removes first occurrence of the element of account
    }

    public Account inquireAccount(String accountNumber) {
        // find and retun account
        return null;
    }
}
