package new_bank.operations;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class bankUserHandler {
    /**
     * A HashMap that stores bank users' information, with the identification number as the key.
     */
    final HashMap<Integer, String[]> bankUsers = new HashMap<>();

    final HashMap<String, Integer> bankUserNames = new HashMap<>();

    final HashMap<Integer, String[]> initialBalances = new HashMap<>();

    /**
     * A HashMap that stores the credit scores for each customer, allowing the score to persist 
     * throughout the session.
     */
    final HashMap<Integer, Integer> creditScores = new HashMap<>();

    final HashMap<Integer, Customer> bankUserCustomer = new HashMap<>();

    /**
     * The path to the CSV file that contains the bank users' information.
     */
    private final String CSV_FILE_PATH = "bank\\info\\Bank Users.csv";

    /**
     * The path to the new CSV file where updated bank users' information will be saved.
     */
    private final String NEW_CSV_FILE_PATH = "info\\New Bank Users.csv";

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
    Map<String, Integer> headerIndexMap = new HashMap<>();

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
    
                int id = Integer.parseInt(lineData[headerIndexMap.get("Identification Number")]);
                String firstName = lineData[headerIndexMap.get("First Name")];
                String lastName = lineData[headerIndexMap.get("Last Name")];
                String dob = lineData[headerIndexMap.get("Date of Birth")];
                String address = lineData[headerIndexMap.get("Address")];
                String phoneNumber = lineData[headerIndexMap.get("Phone Number")];
    
                String checkingAccountNumber = lineData[headerIndexMap.get("Checking Account Number")];
                double checkingBalance = Double.parseDouble(lineData[headerIndexMap.get("Checking Starting Balance")]);
    
                String savingAccountNumber = lineData[headerIndexMap.get("Savings Account Number")];
                double savingBalance = Double.parseDouble(lineData[headerIndexMap.get("Savings Starting Balance")]);
    
                String creditAccountNumber = lineData[headerIndexMap.get("Credit Account Number")];
                double creditMax = Double.parseDouble(lineData[headerIndexMap.get("Credit Max")]);
                double creditBalance = Double.parseDouble(lineData[headerIndexMap.get("Credit Starting Balance")]);
    
                Customer customer = new Customer(firstName, lastName, dob, address, phoneNumber, id,
                                                  checkingAccountNumber, checkingBalance,
                                                  savingAccountNumber, savingBalance,
                                                  creditAccountNumber, creditBalance, creditMax);
    
                bankUsers.put(id, lineData);
                bankUserNames.put(firstName + " " + lastName, id);
                bankUserCustomer.put(id, customer);
            }
        }
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

    /**
     * Saves the current state of the bank users to a new CSV file.
     * 
     * @throws IOException if there is an error writing to the file
     */
    public void updateBankUsersInformation() throws IOException {
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

    public String getUserInfo() {
        String info = "";
        // String fName, String lName, String dob, String address, String phoneNum, int id
        return info;
    }
}