package bank;
/**
 * Creates customer 
 * @author Epifanio Sarinana
 * @author Kayra Dominguez
 */
public class Customer extends Person {
    private int customerID;

    /**
     * @param name customer name
     * @param address customer address
     * @param customerID customer ID
     */
    public Customer(String fName, String lName, String dob, String address, String phoneNum, int id) {
        super(fName,lName,dob, address, phoneNum);
        customerID = id;
    }

    public int getCustomerID() {
        return customerID;
    }
}
