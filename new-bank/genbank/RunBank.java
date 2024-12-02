package genbank;
import java.io.IOException;
import java.util.Scanner;

public class RunBank {
    /**
     * The command used to exit the program.
     */
    private static final String EXIT_COMMAND = "exit";

    public static void main(String[] args) throws IOException {
        userBankHandler users = new userBankHandler(); 
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
                operations.handleAccess();
            } else if (userSelection.equals("2")) {
                operations = new BankManager();
                operations.handleAccess();
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
