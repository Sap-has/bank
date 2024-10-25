import java.util.ArrayList;

/**
 * Creates customer 
 * @author Epifanio Sarinana
 * @author Kayra Dominguez
 */
public class Customer extends Person {
    private ArrayList<Account> accountList;
    private int customerID;

    /**
     * @param name customer name
     * @param address customer address
     * @param customerID customer ID
     */
    public Customer(String name, String address, int customerID) {
        super(name, address);
        this.accountList = new ArrayList<>();
        this.customerID = customerID;
    }

    /**
     * 
     * @return customer ID
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * @param customerID set customer ID
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * 
     * @param account account to add to customer
     */
    public void addAccount(Account account) {
        accountList.add(account);
    }

    /**
     * 
     * @param account account to remove from customer
     */
    public void removeAccount(Account account) {
        accountList.remove(account);
    }

    /**
     * 
     * @param accountNumber account to inquire about
     * @return account inquired about or null if account is not found
     */
    public Account inquireAccount(String accountNumber) {
        for (Account account : accountList) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null; // if not found
    }
}
