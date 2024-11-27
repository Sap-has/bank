package new_bank.operations;

import java.util.ArrayList;

import new_bank.Accounts.Account;
import new_bank.Accounts.Checking;
import new_bank.Accounts.Credit;
import new_bank.Accounts.Saving;

public class Customer extends Person {
    private int customerID;
    private String pw = "";
    private ArrayList<Account> accounts; // 1. Checking, 2. Saving, 3.Credit

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
        accounts.add(new Checking(checkingAccountNumber, this, checkingBalance, 0.0)); // Replace 0.0 with overdraft limit if applicable

        // Create Saving Account
        accounts.add(new Saving(savingAccountNumber, this, savingBalance, 0.0)); // Replace 0.0 with interest rate if applicable

        // Create Credit Account
        accounts.add(new Credit(creditAccountNumber, this, creditBalance, creditMax, 0)); // Replace 0 with the credit score
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public boolean checkPw(String givenPw) {
        return pw.equals(givenPw);
    }

    public Account openAccount(String account) {
        if(account.equals("1")) return accounts.get(0);
        if(account.equals("2")) return accounts.get(1);
        return accounts.get(2);
    }
    

}
