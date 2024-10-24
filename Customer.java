import java.util.ArrayList;

public class Customer extends Person {
    private ArrayList<Account> accountList;
    private int customerID;

    public Customer(String name, String address, int customerID) {
        super(name, address);
        this.accountList = new ArrayList<>();
        this.customerID = customerID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public void addAccount(Account account) {
        accountList.add(account);
    }

    public void removeAccount(Account account) {
        accountList.remove(account);
    }

    public Account inquireAccount(String accountNumber) {
        for (Account account : accountList) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null; // if not found
    }
}
