package new_bank;

import java.io.IOException;
import java.util.Scanner;

import new_bank.operations.BankManager;
import new_bank.operations.BankOperations;
import new_bank.operations.CustomerOperations;
import new_bank.operations.bankUserHandler;

/**
 * The RunBank class is the entry point of the bank system. It provides the functionality 
 * to load bank users from a CSV file, handle user input, and allow users to access the 
 * bank system as either a customer or a bank manager.
 */
public class RunBank {
    /**
     * The command used to exit the program.
     */
    private static final String EXIT_COMMAND = "exit";

    /**
     * Main method that runs the bank system. It loads the bank users, prompts the user 
     * for access, and handles interactions with customers and bank managers.
     * 
     * @param args command-line arguments (not used)
     * @throws IOException if an error occurs while reading or writing files
     */

    public static void main(String[] args) throws IOException {
        bankUserHandler users = new bankUserHandler(); 
        users.loadBankUsersFromCSV();

        Scanner userInput = new Scanner(System.in);
        System.out.println("Welcome to the Bank System. Enter 'exit' to exit the program.");

        while (true) {
            System.out.println("Are you a Customer or Bank Manager?");
            System.out.println("1. Customer");
            System.out.println("2. Bank Manager");

            String userSelection = userInput.nextLine();
            if (userSelection.equalsIgnoreCase(EXIT_COMMAND)) break;

            BankOperations operations;
            if (userSelection.equals("1")) {
                operations = new CustomerOperations();
                operations.handleUserAccess();
            } else if (userSelection.equals("2")) {
                operations = new BankManager();
                operations.handleUserAccess();
            } else {
                System.out.println("Invalid option. Please choose correctly.");
                continue;
            }
        }

        userInput.close();
        users.updateBankUsersInformation();

        System.out.println("Thank you for using the Bank System.");
    }
}