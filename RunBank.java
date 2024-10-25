import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;

public class RunBank {
    public static void main(String[] args) throws IOException{
        HashMap<Integer, String[]> bankUsers = new HashMap<>();
        Scanner bank_users = new Scanner(new File("info\\Bank Users.csv"));
        Scanner user_in = new Scanner(System.in);
        String user_inS;

        // Load the bank users from CSV file into the HashMap
        String firstLine = bank_users.nextLine(); // Read the header
        while (bank_users.hasNextLine()) {
            String line = bank_users.nextLine();
            String[] information = line.split(",");
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
                handleUser(Integer.parseInt(user_inS), bankUsers, user_in);

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
            performAccountOperations(checkingAccount, user_in);
            break;
        case "2": // Savings Account
            Saving savingAccount = new Saving(userInfo[8], new Customer(userInfo[1] + " " + userInfo[2], userInfo[4], ID), Double.parseDouble(userInfo[9]), 0.02);
            performAccountOperations(savingAccount, user_in);
            break;
        case "3": // Credit Account
            Credit creditAccount = new Credit(userInfo[10], new Customer(userInfo[1] + " " + userInfo[2], userInfo[4], ID), Double.parseDouble(userInfo[12]), Double.parseDouble(userInfo[11]), 0d);
            performAccountOperations(creditAccount, user_in);
            break;
        default:
            System.out.println("Invalid option.");
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
            System.out.println("Your current balance is: " + account.inquireBalance());
            break;
        case "2": // Deposit
            System.out.println("Enter deposit amount:");
            double depositAmount = Double.parseDouble(user_in.nextLine());
            account.deposit(depositAmount);
            System.out.println("Deposit successful. Your new balance is: " + account.inquireBalance());
            break;
        case "3": // Withdraw
            System.out.println("Enter withdraw amount:");
            double withdrawAmount = Double.parseDouble(user_in.nextLine());
            account.withdraw(withdrawAmount);
            System.out.println("Withdraw successful. Your new balance is: " + account.inquireBalance());
            break;
        case "4": // Transfer
            System.out.println("Enter recipient's ID:");
            int recipientID = Integer.parseInt(user_in.nextLine());
            System.out.println("Enter account type to transfer to (1: Checking, 2: Saving, 3: Credit):");
            String recipientAccountType = user_in.nextLine();
            System.out.println("Enter transfer amount:");
            double transferAmount = Double.parseDouble(user_in.nextLine());

            System.out.println("Transfer successful.");
            break;
        default:
            System.out.println("Invalid option.");
    }
}