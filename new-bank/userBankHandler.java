import java.io.FileWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class userBankHandler {
    private final String PW_CSV_PATH = "new-bank\\info\\ID_Passwords.csv";
    static final HashMap<Integer, Customer> bankUserCustomer = new HashMap<>();
    /**
     * A HashMap that stores bank users' information, with the identification number as the key.
     */
    static final HashMap<Integer, String[]> bankUsers = new HashMap<>();

    static final HashMap<String, Integer> bankUserNames = new HashMap<>();

    final static HashMap<Integer, String[]> initialBalances = new HashMap<>();

    /**
     * A HashMap that stores the credit scores for each customer, allowing the score to persist 
     * throughout the session.
     */
    final HashMap<Integer, Integer> creditScores = new HashMap<>();

    /**
     * The path to the CSV file that contains the bank users' information.
     */
    private final String CSV_FILE_PATH = "new-bank\\info\\Bank Users.csv";

    /**
     * The header for the CSV file containing bank user data.
     */
    private String CSV_HEADER = "Identification Number,First Name,Last Name,Date of Birth,Address,Phone Number,"+
                "Checking Account Number,Checking Starting Balance,Savings Account Number,Savings Starting Balance,Credit Account Number,"+
                "Credit Max,Credit Starting Balance";

    /**
     * An array of expected headers in the CSV file.
     */
    private final String[] EXPECTED_HEADERS = {
        "Identification Number", "First Name", "Last Name", "Date of Birth",
        "Address", "Phone Number", "Checking Account Number", "Checking Starting Balance",
        "Savings Account Number", "Savings Starting Balance", "Credit Account Number",
        "Credit Max", "Credit Starting Balance"
    };

    /**
     * A map that holds the indices of the CSV headers for easier access while parsing the file.
     */
    HashMap<String, Integer> headerIndexMap = new HashMap<>();  
    
    /**
     * The path to the new CSV file where updated bank users' information will be saved.
     */
    private final String NEW_CSV_FILE_PATH = "new-bank\\info\\New Bank Users.csv";

    /**
     * Loads bank users from the CSV file into the {@link #bankUsers} map. The method 
     * checks for missing headers and parses the CSV file accordingly.
     * 
     * @throws IOException if the file is empty, if expected headers are missing, or if there is an error reading the file
     */
    public void loadBankUsersFromCSV() throws IOException {
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

                Customer customer = new Customer(orderedData[1], orderedData[2], orderedData[3], orderedData[4], orderedData[5], 
                                                id, orderedData[6], Double.parseDouble(orderedData[7]), orderedData[8], 
                                                Double.parseDouble(orderedData[9]), orderedData[10], Double.parseDouble(orderedData[12]), 
                                                Double.parseDouble(orderedData[11]));
                bankUserCustomer.put(id, customer);
                // Store the ordered data in the map with id as the key
                bankUsers.put(id, orderedData);
                // Store ids in the map with the full name as the key
                bankUserNames.put(orderedData[1] + " " + orderedData[2], id);
                // Store initial balances to use for user transactions file
                String[] balances = {orderedData[7], orderedData[9], orderedData[12]};
                initialBalances.put(id, balances);
            }
        }
        loadPw();
    }

    /**
     * Parses a single line of CSV data. Handles cases of commas within quotes.
     * 
     * @param line the CSV line to parse
     * @return an array of strings representing the parsed values
     */
    public String[] parseCSVLine(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    }

    public void addBankUser(int ID, String[] data) {
        bankUsers.put(ID, data);
    }

    /**
     * Saves the current state of the bank users to a new CSV file.
     * 
     * @throws IOException if there is an error writing to the file
     */
    public void updateBankUsersInformation() throws IOException {
         try (FileWriter writer = new FileWriter(NEW_CSV_FILE_PATH, false)) {
            writer.write(CSV_HEADER + "\n");
            for (HashMap.Entry<Integer, String[]> entry : bankUsers.entrySet()) {
                writer.write(String.join(",", entry.getValue()) + "\n");
            }
            bankUsers.clear(); // Clear hashmap after saving
        } catch (IOException e) {
            System.out.println("Error saving the updated users information: " + e.getMessage());
        }
    }

    public void loadPw() throws FileNotFoundException {
        Scanner pwScanner = new Scanner(new File(PW_CSV_PATH));
        pwScanner.nextLine();
        
        // Parse file and load data
        while (pwScanner.hasNextLine()) {
            String[] data = pwScanner.nextLine().split(",");
            int id = Integer.parseInt(data[0]);
            String password = data[1];
            
            // Update password in the Customer object
            Customer customer = bankUserCustomer.get(id);
            if (customer != null) {
                customer.setPw(password);
                bankUserCustomer.put(id, customer);
            } else {
                System.out.println("No customer found for ID: " + id);
            }
        }
        pwScanner.close();
    }
}
