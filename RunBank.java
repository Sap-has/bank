import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.util.Random; // for credit score

// improve the log transactions for all methods
// get length of hashmap - will be id of new customer
// generate a random number for credit score


public class RunBank {
    static final HashMap<Integer, String[]> bankUsers = new HashMap<>();
    static final HashMap<Integer, Integer> creditScores = new HashMap<>(); // map to keep track of creditScore (allows for score to persist for duration of session)
    private static final String CSV_FILE_PATH = "info\\Bank Users.csv";
    private static String CSV_HEADER = "Identification Number,First Name,Last Name,Date of Birth,Address,Phone Number,"+
                "Checking Account Number,Checking Starting Balance,Savings Account Number,Savings Starting Balance,Credit Account Number,"+
                "Credit Max,Credit Starting Balance";
    private static final String[] EXPECTED_HEADERS = {
        "Identification Number", "First Name", "Last Name", "Date of Birth",
        "Address", "Phone Number", "Checking Account Number", "Checking Starting Balance",
        "Savings Account Number", "Savings Starting Balance", "Credit Account Number",
        "Credit Max", "Credit Starting Balance"
    };
    static Map<String, Integer> headerIndexMap = new HashMap<>();
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
            }
        }
    }

    public static String[] parseCSVLine(String line) {
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