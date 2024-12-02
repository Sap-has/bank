import java.util.ArrayList;

public class Customer extends Person {
    private int customerID;
    private String pw = "";
    private ArrayList<Account> accounts;

    /**
     * @param name customer name
     * @param address customer address
     * @param customerID customer ID
     */
    public Customer(String fName, String lName, String dob, String address, String phoneNum, int id, String checkingAccountNumber, double checkingBalance,
                    String savingAccountNumber, double savingBalance,
                    String creditAccountNumber, double creditBalance, double creditMax) {
        super(fName, lName, dob, address, phoneNum);
        this.customerID = id;
        this.accounts = new ArrayList<>();

        // Create Checking Account
        accounts.add(new Checking(checkingAccountNumber, this, checkingBalance)); 

        // Create Saving Account
        accounts.add(new Saving(savingAccountNumber, this, savingBalance)); 

        // Create Credit Account
        accounts.add(new Credit(creditAccountNumber, this, creditBalance, creditMax, 0)); // Replace 0 with the credit score
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }
    
    public String getPw() {
        return pw;
    }

    public Account openAccount(String account) {
        if(account.equals("1")) return accounts.get(0);
        if(account.equals("2")) return accounts.get(1);
        return accounts.get(2);
    }
}