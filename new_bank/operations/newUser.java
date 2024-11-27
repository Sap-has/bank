package new_bank.operations;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class newUser {
    CustomerOperations customerOperations = new CustomerOperations();
    private final HashMap<Integer, String[]> bankUsers = customerOperations.bankUsers; // Reference from RunBank for simplicity
    Scanner userInput = new Scanner(System.in);
    private final String EXIT_COMMAND = "exit";
    /**
     * Prompts the user to indicate whether they are an existing customer or want to set up a new account.
     * If setting up a new account, it collects customer details and adds them to the system.
     */
    public void isNewUser() {
        while(true) {
            System.out.println("Are you a (1) customer with us or (2) do you want to set up an account?");
            String newUser = userInput.nextLine();
            if (newUser.equalsIgnoreCase(EXIT_COMMAND)) return;

            if("1".equalsIgnoreCase(newUser)) return;
            if("2".equals(newUser)) {
                System.out.println("Please provide the following information to set up your account.");

                System.out.println("First Name:");
                String input = userInput.nextLine();
                if (input.equalsIgnoreCase(EXIT_COMMAND)) return;
                String firstName = userInput.nextLine();

                System.out.println("Last Name:");
                input = userInput.nextLine();
                if (input.equalsIgnoreCase(EXIT_COMMAND)) return;
                String lastName = userInput.nextLine();

                System.out.println("Date of Birth (Day-Month-Year):");
                input = userInput.nextLine();
                if (input.equalsIgnoreCase(EXIT_COMMAND)) return;
                String dateOfBirth = userInput.nextLine();

                System.out.println("Address (Street, City, State Zipcode):");
                input = userInput.nextLine();
                if (input.equalsIgnoreCase(EXIT_COMMAND)) return;
                String address = userInput.nextLine();

                System.out.println("Phone Number (format: xxx-xxx-xxxx):");
                input = userInput.nextLine();
                if (input.equalsIgnoreCase(EXIT_COMMAND)) return;
                String phoneNum = userInput.nextLine();

                Customer newCustomer = new Customer(firstName, lastName, dateOfBirth, address, phoneNum, bankUsers.size()+1);
                addNewUser(newCustomer);
                return;
            }
        }
    }

    /**
     * Adds a new customer to the bank system and generates account information for them.
     * The customer's details are saved both in memory and appended to a CSV file for persistence.
     *
     * @param newCustomer The new customer to be added.
     */
    public void addNewUser(Customer newCustomer) {
        String address = "\"" + newCustomer.getAddress() + "\"";
        String[] userInfo = {
            Integer.toString(newCustomer.getCustomerID()),
            newCustomer.getFirstName(),
            newCustomer.getLastName(),
            newCustomer.getDateOfBirth(),
            address,
            newCustomer.getPhoneNum()
        };
        userInfo = createAccounts(userInfo); // add account numbers and balances
        bankUsers.put(newCustomer.getCustomerID(), userInfo);
    
        // Optionally, update the CSV file to save the new user data
        appendNewUserToCSV(userInfo);
        System.out.println("New user added successfully. Your ID is " + newCustomer.getCustomerID());
    }

    /**
     * Generates account numbers for a new customer and updates their account information.
     *
     * @param userInfo The user's personal information to which account information will be appended.
     * @return The updated user information with account numbers and balances.
     */
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
     * Generates a unique account number for a specified account type (checking, saving, or credit).
     *
     * @param accountType The type of account (1 for checking, 2 for saving, 3 for credit).
     * @return A unique account number for the specified account type.
     */
    private int generateUniqueAccountNumber(int accountType) {
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
    
        return maxAccountNumber + 1; // Return the next unique account number
    }

    /**
     * Appends new user information to the CSV file to persist changes made to the bank's user database.
     *
     * @param userInfo The user information to be written to the CSV file.
     */
    private void appendNewUserToCSV(String[] userInfo) {
        try (FileWriter writer = new FileWriter(CSV_FILE_PATH, true)) {
            writer.write(String.join(",", userInfo) + "\n");
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }
}
