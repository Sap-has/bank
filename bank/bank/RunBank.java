package bank;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.util.Random; 

/**
 * The RunBank class is the entry point of the bank system. It provides the functionality 
 * to load bank users from a CSV file, handle user input, and allow users to access the 
 * bank system as either a customer or a bank manager.
 */
public class RunBank {

    /**
     * A HashMap that stores bank users' information, with the identification number as the key.
     */
    static final HashMap<Integer, String[]> bankUsers = new HashMap<>();

    static final HashMap<String, Integer> bankUserNames = new HashMap<>();

    static final HashMap<Integer, String[]> initialBalances = new HashMap<>();

    /**
     * A HashMap that stores the credit scores for each customer, allowing the score to persist 
     * throughout the session.
     */
    static final HashMap<Integer, Integer> creditScores = new HashMap<>();

    /**
     * The path to the CSV file that contains the bank users' information.
     */
    private static final String CSV_FILE_PATH = "info\\Bank Users.csv";

    /**
     * The header for the CSV file containing bank user data.
     */
    private static String CSV_HEADER = "Identification Number,First Name,Last Name,Date of Birth,Address,Phone Number,"+
                "Checking Account Number,Checking Starting Balance,Savings Account Number,Savings Starting Balance,Credit Account Number,"+
                "Credit Max,Credit Starting Balance";

    /**
     * An array of expected headers in the CSV file.
     */
    private static final String[] EXPECTED_HEADERS = {
        "Identification Number", "First Name", "Last Name", "Date of Birth",
        "Address", "Phone Number", "Checking Account Number", "Checking Starting Balance",
        "Savings Account Number", "Savings Starting Balance", "Credit Account Number",
        "Credit Max", "Credit Starting Balance"
    };

    /**
     * A map that holds the indices of the CSV headers for easier access while parsing the file.
     */
    static Map<String, Integer> headerIndexMap = new HashMap<>();

    /**
     * The command used to exit the program.
     */
    private static final String EXIT_COMMAND = "exit";

    /**
     * The path to the new CSV file where updated bank users' information will be saved.
     */
    private static final String NEW_CSV_FILE_PATH = "info\\New Bank Users.csv";

    /**
     * Main method that runs the bank system. It loads the bank users, prompts the user 
     * for access, and handles interactions with customers and bank managers.
     * 
     * @param args command-line arguments (not used)
     * @throws IOException if an error occurs while reading or writing files
     */
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

    /**
     * Loads bank users from the CSV file into the {@link #bankUsers} map. The method 
     * checks for missing headers and parses the CSV file accordingly.
     * 
     * @throws IOException if the file is empty, if expected headers are missing, or if there is an error reading the file
     */
    static void loadBankUsersFromCSV() throws IOException {
        try (Scanner bankFileScanner = new Scanner(new File(CSV_FILE_PATH))) {
            if (!bankFileScanner.hasNextLine()) {
                throw new IOException("CSV file is empty.");
            }

            // Read header and create index map
            String[] headers = bankFileScanner.nextLine().split(",");
            for (int i = 0; i < headers.length; i++) {
                headerIndexMap.put(headers[i].trim(), i);
            }

            // Check if all expected headers are present
            for (String expectedHeader : EXPECTED_HEADERS) {
                if (!headerIndexMap.containsKey(expectedHeader)) {
                    throw new IOException("Missing expected header: " + expectedHeader);
                }
            }

            // Read each user row, using the header index map to parse fields
            while (bankFileScanner.hasNextLine()) {
                String[] lineData = parseCSVLine(bankFileScanner.nextLine());

                // Retrieve the Identification Number using the index map
                int idIndex = headerIndexMap.get("Identification Number");
                int id = Integer.parseInt(lineData[idIndex]);

                // Create a new array to store data in the original header order
                String[] orderedData = new String[EXPECTED_HEADERS.length];
                for (int i = 0; i < EXPECTED_HEADERS.length; i++) {
                    orderedData[i] = lineData[headerIndexMap.get(EXPECTED_HEADERS[i])];
                }

                // Store the ordered data in the map with id as the key
                bankUsers.put(id, orderedData);
                // Store ids in the map with the full name as the key
                bankUserNames.put(orderedData[1] + " " + orderedData[2], id);
                // Store initial balances to use for user transactions file
                String[] balances = {orderedData[7], orderedData[9], orderedData[12]};
                initialBalances.put(id, balances);
            }
        }
    }

    /**
     * Parses a single line of CSV data. Handles cases of commas within quotes.
     * 
     * @param line the CSV line to parse
     * @return an array of strings representing the parsed values
     */
    public static String[] parseCSVLine(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    }

    /**
     * Saves the current state of the bank users to a new CSV file.
     * 
     * @throws IOException if there is an error writing to the file
     */
    private static void updateBankUsersInformation() throws IOException {
         try (FileWriter writer = new FileWriter(NEW_CSV_FILE_PATH, false)) {
            writer.write(CSV_HEADER + "\n");
            for (Map.Entry<Integer, String[]> entry : bankUsers.entrySet()) {
                writer.write(String.join(",", entry.getValue()) + "\n");
            }
            bankUsers.clear(); // Clear hashmap after saving
        } catch (IOException e) {
            System.out.println("Error saving the updated users information: " + e.getMessage());
        }
    }

    /**
     * Opens a new bank account based on the provided account type.
     * 
     * @param accountType the type of account to open (1 for Checking, 2 for Saving, 3 for Credit)
     * @param userInfo the user's information as an array of strings
     * @param customerId the unique identification number for the customer
     * @return an {@link Account} object for the newly created account, or null if the account type is invalid
     */
    public static Account openAccount(String accountType, String[] userInfo, int customerId) {
        Customer customer = new Customer(userInfo[1], userInfo[2], userInfo[3], userInfo[4], userInfo[5], customerId);
    
        switch (accountType) {
            case "1": // Checking
                return new Checking(userInfo[6], customer, Double.parseDouble(userInfo[7]), 500);
            case "2": // Saving
                return new Saving(userInfo[8], customer, Double.parseDouble(userInfo[9]), 0.02);
            case "3": // Credit
                // Generate or retrieve a persistent credit score for the session
                int creditScore = creditScores.computeIfAbsent(customerId, k -> new Random().nextInt(851) + 300); // scores from 300-850 (inclusive)
                return new Credit(userInfo[10], customer, Double.parseDouble(userInfo[12]), Double.parseDouble(userInfo[11]), creditScore);
            default:
                return null;
        }
    }
}
