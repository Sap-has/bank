package genbank;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class User {
    private final String EXIT_COMMAND = "exit";
    private final HashMap<Integer, Customer> bankUserCustomer = userBankHandler.bankUserCustomer;
    private final HashMap<Integer, String[]> bankUsers = userBankHandler.bankUsers;
    private final HashMap<String, Integer> bankuserNames = userBankHandler.bankUserNames;
    private static final Map<String, Integer> headerIndexMap = userBankHandler.headerIndexMap;
    AccountOperations accountOperations = new AccountOperations();
    private final String CSV_FILE_PATH = "new-bank\\info\\Bank Users.csv";
    Customer customer;
    
    Scanner userInput = new Scanner(System.in);
    public void handleNewUser() {
        String[] info = newUser();
        int creditScore = creditScoreValidation();
        double creditLimit = accountOperations.calculateCreditLimit(creditScore);
        info = createAccounts(info);
        info[12] = Double.toString(creditLimit);
        addNewUser(info, new Customer(info[0], info[1], info[2], info[3], info[4], Integer.parseInt(info[5]), info[6], Double.parseDouble(info[7]), info[8], Double.parseDouble(info[9]), info[10], Double.parseDouble(info[11]), Double.parseDouble(info[12])));
    }

    public int creditScoreValidation() {
        int creditScore = 0;
        boolean isValid = false;

        while (!isValid) {
            System.out.println("What is your credit score? (Enter a number between 300 and 850)");
            if (userInput.hasNextInt()) {
                creditScore = userInput.nextInt();
                if (creditScore >= 300 && creditScore <= 850) {
                    isValid = true;
                } else {
                    System.out.println("Invalid input. Credit score must be between 300 and 850.");
                }
            } else {
                System.out.println("Invalid input. Please enter a numeric value.");
                userInput.next(); // Clear the invalid input
            }
        }
        return creditScore;
    }

    public String[] newUser() {
        System.out.println("Please provide the following information to set up your account.");

        System.out.println("First Name:");
        String firstName = userInput.nextLine();

        System.out.println("Last Name:");
        String lastName = userInput.nextLine();

        System.out.println("Date of Birth (Day-Month-Year):");
        String dateOfBirth = userInput.nextLine();

        System.out.println("Address (Street, City, State Zipcode):");
        String address = userInput.nextLine();

        System.out.println("Phone Number (format: xxx-xxx-xxxx):");
        String phoneNum = userInput.nextLine();

        String[] arr = {firstName, lastName, dateOfBirth, address, phoneNum, Integer.toString(bankUsers.size()+1)};

        return arr;
    }

    public int generateUniqueAccountNumber(int accountType) {
        int maxAccountNumber = 1000; // Starting point if no accounts exist

        for (String[] data : bankUsers.values()) {
            int checkingAccountNum = Integer.parseInt(data[headerIndexMap.get("Checking Account Number")]);
            int savingAccountNum = Integer.parseInt(data[headerIndexMap.get("Savings Account Number")]);
            int creditAccountNum = Integer.parseInt(data[headerIndexMap.get("Credit Account Number")]);
            
            switch (accountType) {
                case 1:
                    maxAccountNumber = Math.max(maxAccountNumber, checkingAccountNum);
                    break;
                case 2:
                    maxAccountNumber = Math.max(maxAccountNumber, savingAccountNum);
                    break;
                default:
                    maxAccountNumber = Math.max(maxAccountNumber, creditAccountNum);
                    break;
            }
        }
        return maxAccountNumber;
    }

    public String[] createAccounts(String[] userInfo) {
        int checkingAccountNum = generateUniqueAccountNumber(1);
        int savingAccountNum = generateUniqueAccountNumber(2);
        int creditAccountNum = generateUniqueAccountNumber(3);

        // Append account numbers and starting balances to userInfo array
        String[] updatedUserInfo = new String[userInfo.length + 7];
        System.arraycopy(userInfo, 0, updatedUserInfo, 0, userInfo.length);
        updatedUserInfo[userInfo.length] = Integer.toString(checkingAccountNum);
        updatedUserInfo[userInfo.length + 1] = "0"; // Checking Starting Balance
        updatedUserInfo[userInfo.length + 2] = Integer.toString(savingAccountNum);
        updatedUserInfo[userInfo.length + 3] = "0"; // Savings Starting Balance
        updatedUserInfo[userInfo.length + 4] = Integer.toString(creditAccountNum);
        updatedUserInfo[userInfo.length + 5] = "0"; // Credit max
        updatedUserInfo[userInfo.length + 6] = "0"; // Credit Starting Balance

        return updatedUserInfo;
    }

    /**
     * Adds a new customer to the bank system and generates account information for them.
     * The customer's details are saved both in memory and appended to a CSV file for persistence.
     *
     * @param newCustomer The new customer to be added.
     */
    public void addNewUser(String[] info, Customer newCustomer) {
        bankUsers.put(newCustomer.getCustomerID(), info);
    
        // Optionally, update the CSV file to save the new user data
        appendNewUserToCSV(info);
        System.out.println("New user added successfully. Your ID is " + newCustomer.getCustomerID());
    }

    /**
     * Appends new user information to the CSV file to persist changes made to the bank's user database.
     *
     * @param userInfo The user information to be written to the CSV file.
     */
    public void appendNewUserToCSV(String[] userInfo) {
        try (FileWriter writer = new FileWriter(CSV_FILE_PATH, true)) {
            writer.write(String.join(",", userInfo) + "\n");
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }


    // handle new user creation
    // handle new password creation
    public Customer handleLogIn() {
        while (true) {
            System.out.println("To log in, provide either your full name (first name last name) or ID:");
            String input = userInput.nextLine();
            if (input.equalsIgnoreCase(EXIT_COMMAND)) return null;

            Customer customer = fetchCustomer(input);
            if (customer != null) {
                if (authenticateCustomer(customer)) {
                    System.out.println("Login successful!");
                    return customer;
                } else {
                    System.out.println("Incorrect password.");
                }
            } else {
                System.out.println("User not found. Please try again.");
            }
        }
    }

    public Customer fetchCustomer(String input) {
        if (input.matches("\\d+")) { // 1 or more digits
            int userId = Integer.parseInt(input);
            if (bankUsers.containsKey(userId)) {
                return bankUserCustomer.get(userId);
            }
        } else if (input.matches("[a-zA-Z]+\\s[a-zA-Z]+")) { // firstName(a-z) (space) lastName(a-z)
            if (bankuserNames.containsKey(input.toLowerCase())) {
                return bankUserCustomer.get(bankuserNames.get(input));
            }
        }
        return null;
    }

    public boolean authenticateCustomer(Customer customer) {
        System.out.println("Enter your password:");
        String password = userInput.nextLine();
        return customer.getPw().equals(password);
    }
}
