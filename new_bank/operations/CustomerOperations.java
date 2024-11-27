package new_bank.operations;

import java.util.HashMap;
import java.util.Scanner;

import new_bank.Accounts.Account;

public class CustomerOperations implements BankOperations {
    bankUserHandler userHandler = new bankUserHandler();
    final HashMap<Integer, String[]> bankUsers = userHandler.bankUsers; // Reference from RunBank for simplicity
    private final HashMap<String, Integer> bankUserNames = userHandler.bankUserNames;
    public final HashMap<Integer, Customer> bankUserCustomer = userHandler.bankUserCustomer;
    private final String EXIT_COMMAND = "exit";

    Scanner userInput = new Scanner(System.in);
    Customer customer;
    Account account;

    @Override
    /**
     * Prompts the user to enter their customer ID and provides access to their accounts if the ID exists.
     * If the user is a new customer, they are prompted to provide their information to set up a new account.
     */
    public void handleUserAccess() {
        int customerId;
        String input;
        while(true) {
            System.out.println("Enter the customer ID or Full Name (First Name Last Name):");
            input = userInput.nextLine();
            if (input.equalsIgnoreCase(EXIT_COMMAND)) return;
           
            if (input.split(" ").length == 2){
                //find customer id with full name in hashmap and make input the id
                if(bankUserNames.get(input) == null) {
                    System.out.println("Not a valid name, please try again");
                    continue;
                }
                customerId = bankUserNames.get(input);
            } else {
                try {
                    customerId = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Not a valid entry");
                    continue;
                }

                if(bankUsers.get(customerId) == null) {
                    System.out.println("Not a valid ID, please try again");
                    continue;
                }
            }
            String customerName = openAccounts(customerId);
            boolean validPassword = promptUserPassword(customerId);
            if(!validPassword) {
                System.out.println("Not a valid password");
                promptUserPassword(customerId);
                continue;
            }

            System.out.print("Welcome " + customerName + " ");
            String accountNumber = selectAccount(customerId);
            int operation = selectOperation(customerId);

        } // while loop ends

    }   // handleUserAccess ends

    boolean promptUserPassword(int id) {
        if(customer.getPw().equals("")) createNewPw();
        System.out.println("Please give password for account");

        String pw = userInput.nextLine();
        return customer.checkPw(pw);
    }

    void createNewPw() {
        System.out.println("Please enter a password (5 characters):");
        String input = userInput.nextLine();
        while(input.length() != 5) {
            System.out.println("Please enter a password (5 characters):");
            input = userInput.nextLine();
        }
        customer.setPw(input);
    }

    public String openAccounts(int customerId) {
        customer = bankUserCustomer.get(customerId);
        return customer.getName();
    }

    String selectAccount(int id) {
        System.out.println("which account do you want to access?\n(1) Checking\n(2) Saving\n(3) Credit");
        String input = userInput.nextLine();
        while(!input.equals("1")&&!input.equals("2")&&!input.equals("3")) {
            System.out.println("Which account do you want to access?\n(1) Checking\n(2) Saving\n(3) Credit");
            input = userInput.nextLine();
        }

        account = customer.openAccount(input);
        return getAccountNumber(account);
    }

    String getAccountNumber(Account acc) {
        return acc.getAccountNumber();
    }

    int selectOperation(int id) {
        System.out.println("(1) Inquire Balance\n(2)Deposit\n(3)Withdraw\n(4)Transfer\n(5)Pay\n(6)Generate Transaction File");
        return 1;
    }
}
