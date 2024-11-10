import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;

// improve the log transactions for all methods
// get length of hashmap - will be id of new customer
// generate a random number for credit score


public class RunBank {
    static final HashMap<Integer, String[]> bankUsers = new HashMap<>();
    private static final String CSV_FILE_PATH = "info\\Bank Users.csv";
    private static String CSV_HEADER = "";
    private static final String EXIT_COMMAND = "exit";
    private static final String NEW_CSV_FILE_PATH = "info\\New Bank Users.csv";

    public static void main(String[] args) throws IOException {
        loadBankUsersFromCSV();
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
                operations = new CustomerOperation();
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
        updateBankUsersInformation();
        System.out.println("Thank you for using the Bank System.");
    }

    static void loadBankUsersFromCSV() throws IOException {
        Scanner bankFileScanner = new Scanner(new File(CSV_FILE_PATH));
        CSV_HEADER = bankFileScanner.nextLine(); // Skip header

        while (bankFileScanner.hasNextLine()) {
            String[] userInfo = parseCSVLine(bankFileScanner.nextLine());
            bankUsers.put(Integer.parseInt(userInfo[0]), userInfo);
        }

        bankFileScanner.close();
    }

    private static String[] parseCSVLine(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    }

    private static void updateBankUsersInformation() throws IOException {
         try (FileWriter writer = new FileWriter(NEW_CSV_FILE_PATH, false)) {
            writer.write(CSV_HEADER+"\n");
            for (Map.Entry<Integer, String[]> entry : bankUsers.entrySet()) {
                writer.write(String.join(",", entry.getValue()) + "\n");
            }
            bankUsers.clear(); // Clear hashmap after saving
        } catch (IOException e) {
            System.out.println("Error saving the updated users information: " + e.getMessage());
        }
    }

    public static Account openAccount(String accountType, String[] userInfo, int customerId) {
        Customer customer = new Customer(userInfo[1] + " " + userInfo[2], userInfo[4], customerId);
    
        switch (accountType) {
            case "1": // Checking
                return new Checking(userInfo[6], customer, Double.parseDouble(userInfo[7]), 500);
            case "2": // Saving
                return new Saving(userInfo[8], customer, Double.parseDouble(userInfo[9]), 0.02);
            case "3": // Credit
                return new Credit(userInfo[10], customer, Double.parseDouble(userInfo[12]), Double.parseDouble(userInfo[11]), 0, 0);
            default:
                return null;
        }
    }
}