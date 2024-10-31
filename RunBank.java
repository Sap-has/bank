import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;

// make sure throw error when getting over the balance
// Go back functionality in console
// Bank manager can only inquire balance
    // create class
// create the new csv file for bankusers
// update class diagram
    // scenario
    // use case
// ask about overdraft 
// improve 

public class RunBank {
    static HashMap<Integer, String[]> bankUsers = new HashMap<>();
    static int x=0;
    public static void main(String[] args) throws IOException{
        
        Scanner bank_users = new Scanner(new File("info\\Bank Users.csv"));
        Scanner user_in = new Scanner(System.in);
        String user_inS;

        // Load the bank users from CSV file into the HashMap
        String firstLine = bank_users.nextLine(); // Read the header
        while (bank_users.hasNextLine()) {
            String line = bank_users.nextLine();
            String[] information = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            bankUsers.put(Integer.parseInt(information[0]), information);
        }
        bank_users.close();

        System.out.println("Welcome to the Bank System");
        System.out.println("Enter 'exit' to exit the program");

        do {
            System.out.println("Are you a User or Bank Manager?");
            System.out.println("1. User");
            System.out.println("2. Bank Manager");

            user_inS = user_in.nextLine();

            if (user_inS.equalsIgnoreCase("exit")) {
                break;
            }
            if (user_inS.equals("1")) { // Customer actions
                System.out.println("Inquire about your account by inputting your ID:");
                user_inS = user_in.nextLine();
                int id = Integer.parseInt(user_inS);
                if (bankUsers.containsKey(id)) {
                    handleUser(id, bankUsers, user_in);
                } else {
                    System.out.println("ID does not exist.");
                }
            } else if (user_inS.equals("2")) { // Bank manager actions
                System.out.println("Please input the customer ID you want to access:");
                user_inS = user_in.nextLine();
                int id = Integer.parseInt(user_inS);
                if (bankUsers.containsKey(id)) {
                    handleUser(id, bankUsers, user_in);
                } else {
                    System.out.println("ID does not exist.");
                }
            } else {
                System.out.println("Please choose correctly.");
            }
        } while (!user_inS.equalsIgnoreCase("exit"));

        user_in.close();
        System.out.println("Thank you for using the Bank System.");
    }


    public static void handleUser(int ID, HashMap<Integer, String[]> bankUsers, Scanner user_in) {
        String[] userInfo = bankUsers.get(ID);

        System.out.println("Which account would you like to access?");
        System.out.println("1. Checking");
        System.out.println("2. Saving");
        System.out.println("3. Credit");

        String userChoice = user_in.nextLine();

        switch (userChoice) {
            case "1": // Checking Account
                Checking checkingAccount = new Checking(userInfo[6], new Customer(userInfo[1] + " " + userInfo[2], userInfo[4], ID), Double.parseDouble(userInfo[7]), 500d);
                x = 7;
                performAccountOperations(checkingAccount, user_in);
                break;
            case "2": // Savings Account
                Saving savingAccount = new Saving(userInfo[8], new Customer(userInfo[1] + " " + userInfo[2], userInfo[4], ID), Double.parseDouble(userInfo[9]), 0.02);
                x = 9;
                performAccountOperations(savingAccount, user_in);
                break;
            case "3": // Credit Account
                Credit creditAccount = new Credit(userInfo[10], new Customer(userInfo[1] + " " + userInfo[2], userInfo[4], ID), Double.parseDouble(userInfo[12]), Double.parseDouble(userInfo[11]), 0d);
                x = 12;
                performAccountOperations(creditAccount, user_in);
                break;
            default:
                System.out.println("Invalid option.");
                break;
        }
    }

    public static void performAccountOperations(Account account, Scanner user_in) {
        System.out.println("Hello " + account.getOwner().getName());
        System.out.println("What would you like to do?");
        System.out.println("1. Inquire Balance");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");

        String action = user_in.nextLine();

        switch (action) {
            case "1": // Inquire Balance
                System.out.println(account.inquireBalance());
                break;
            case "2": // Deposit
                handleDeposit(account, user_in);
                break;
            case "3": // Withdraw
                handleWithdraw(account, user_in);
                break;
            case "4": // Transfer
                handleTransfer(account, user_in);
                break;
            default:
                System.out.println("Invalid option.");
                break;
        }
    }

    private static void handleTransfer(Account account, Scanner user_in) {
        // deposit to other account
        // withdraw persons account
        System.out.println("Enter recipient's ID:");
        int recipientID = Integer.parseInt(user_in.nextLine());
        if (!bankUsers.containsKey(recipientID)) {
            System.out.println("ID does not exist.");
        } else {
            String[] userInfo = bankUsers.get(recipientID);
            System.out.println("Enter account type to transfer to (1: Checking, 2: Saving, 3: Credit):");
            String recipientAccountType = user_in.nextLine();
            double transferAmount = 0;
            switch (recipientAccountType) {
                case "1": // Checking Account
                    Checking checkingAccount = new Checking(userInfo[6], new Customer(userInfo[1] + " " + userInfo[2], userInfo[4], recipientID), Double.parseDouble(userInfo[7]), 500d);
                    x = 7;
                    System.out.println("Enter transfer amount:");
                    transferAmount = Double.parseDouble(user_in.nextLine());
                    try {
                        account.withdraw(transferAmount);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    checkingAccount.deposit(transferAmount);
                    break;
                case "2": // Savings Account
                    Saving savingAccount = new Saving(userInfo[8], new Customer(userInfo[1] + " " + userInfo[2], userInfo[4], recipientID), Double.parseDouble(userInfo[9]), 0.02);
                    x = 9;
                    System.out.println("Enter transfer amount:");
                    transferAmount = Double.parseDouble(user_in.nextLine());
                    try {
                        account.withdraw(transferAmount);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    savingAccount.deposit(transferAmount);
                    break;
                case "3": // Credit Account
                    Credit creditAccount = new Credit(userInfo[10], new Customer(userInfo[1] + " " + userInfo[2], userInfo[4], recipientID), Double.parseDouble(userInfo[12]), Double.parseDouble(userInfo[11]), 0d);
                    x = 12;
                    System.out.println("Enter transfer amount:");
                    transferAmount = Double.parseDouble(user_in.nextLine());
                    try {
                        account.withdraw(transferAmount);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    creditAccount.deposit(transferAmount);
                    break;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
            System.out.println("Transfer successful.");
        }
        

        
        
    }


    private static void handleWithdraw(Account account, Scanner user_in) {
        System.out.println("Enter withdraw amount:");
        double withdrawAmount = Double.parseDouble(user_in.nextLine());
        try {
            account.withdraw(withdrawAmount);
            double newBalance = Double.parseDouble(bankUsers.get(account.getOwner().getCustomerID())[x]) - withdrawAmount;
            String[] updatedBalance = bankUsers.get(account.getOwner().getCustomerID());
            updatedBalance[x] = Double.toString(newBalance);
            bankUsers.put((Integer)account.getOwner().getCustomerID(), updatedBalance);
            System.out.println(account.inquireBalance());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public static void handleDeposit(Account account, Scanner user_in) {
        System.out.println("Enter deposit amount:");
        double depositAmount = Double.parseDouble(user_in.nextLine());
        account.deposit(depositAmount);
        double newBalance = Double.parseDouble(bankUsers.get(account.getOwner().getCustomerID())[x]) + depositAmount;
        String[] updatedBalance = bankUsers.get(account.getOwner().getCustomerID());
        updatedBalance[x] = Double.toString(newBalance);
        bankUsers.put((Integer)account.getOwner().getCustomerID(), updatedBalance);
        System.out.println(account.inquireBalance());
    }
}