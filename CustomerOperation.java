import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * The {@code CustomerOperation} class implements the {@code BankOperations} interface and provides 
 * functionality for customers to access and manage their bank accounts. It handles tasks such as 
 * account selection, balance inquiry, deposits, withdrawals, and transfers.
 * This class allows for setting up new customers and saving their information to the CSV file.
 */
public class CustomerOperation implements BankOperations {
    private static final HashMap<Integer, String[]> bankUsers = RunBank.bankUsers; // Reference from RunBank for simplicity
    private static final HashMap<String, Integer> bankUserNames = RunBank.bankUserNames;
    private static final Map<String, Integer> headerIndexMap = RunBank.headerIndexMap;
    private static final String CSV_FILE_PATH = "info\\Bank Users.csv";
    private static final String EXIT_COMMAND = "exit";
    private static int balanceIndex;
    Scanner userInput = new Scanner(System.in);

    /**
     * Prompts the user to enter their customer ID and provides access to their accounts if the ID exists.
     * If the user is a new customer, they are prompted to provide their information to set up a new account.
     */
    public void handleUserAccess() {
        isNewUser();
        int customerId;
        System.out.println("Enter the customer ID or Full Name (First Name Last Name):");
        String input = userInput.nextLine();
        if (input.equalsIgnoreCase(EXIT_COMMAND)) return;
        if (input.split(" ").length == 2){
            //find customer id with full name in hashmap and make input the id
            customerId = bankUserNames.get(input);
        }else{
            customerId = Integer.parseInt(input);
        }
        //TODO: handle errors if not name lol uwu

        if (!bankUsers.containsKey(customerId)) {
            System.out.println("ID does not exist.");
            return;
        }
        selectAccountAndPerformOperations(customerId);
    }

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

    /**
     * Prompts the customer to select an account type and perform operations such as balance inquiry, deposits, 
     * withdrawals, and transfers.
     *
     * @param customerId The ID of the customer whose account operations are to be performed.
     */
    public void selectAccountAndPerformOperations(int customerId) {
        String[] userInfo = bankUsers.get(customerId);

        System.out.println("Select an account to access:");
        System.out.println("1. Checking");
        System.out.println("2. Saving");
        System.out.println("3. Credit");

        String accountType = userInput.nextLine();
        while(!accountType.equalsIgnoreCase(EXIT_COMMAND)) {
            Account account = RunBank.openAccount(accountType, userInfo, customerId);
            switch (accountType) {
                case "1": // Checking
                    balanceIndex = 7; 
                    break;
                case "2": // Saving
                    balanceIndex = 9; 
                    break;
                case "3": // Credit
                    balanceIndex = 12;
                    break;
                default:
                    break;
            }
            if (account != null) {
                performAccountOperations(account);
            } else {
                System.out.println("Invalid account type.");
            }
            System.out.println("Select an account to access:");
            System.out.println("1. Checking");
            System.out.println("2. Saving");
            System.out.println("3. Credit");
            accountType = userInput.nextLine();
        }
    }

    /**
     * Performs the selected operation (inquire balance, deposit, withdraw, or transfer) on the specified account.
     *
     * @param account The account on which the operations will be performed.
     */
    private void performAccountOperations(Account account) {
        System.out.println("Hello " + account.getOwner().getName());
        System.out.println("Choose an action:");
        System.out.println("1. Inquire Balance");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");

        String action = userInput.nextLine();
        if (action.equalsIgnoreCase(EXIT_COMMAND)) return;

        switch (action) {
            case "1":
                account.inquireBalance();
                break;
            case "2":
                handleTransaction(account, true);
                break;
            case "3":
                handleTransaction(account, false);
                break;
            case "4":
                handleTransfer(account);
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    /**
     * Handles the transaction (deposit or withdrawal) for a given account.
     *
     * @param account    The account on which the transaction will be performed.
     * @param isDeposit If true, it's a deposit; if false, it's a withdrawal.
     */
    public void handleTransaction(Account account, boolean isDeposit) {
        System.out.println("Enter amount:");
        String input = userInput.nextLine();
        if (input.equalsIgnoreCase(EXIT_COMMAND)) return;

        double amount = Double.parseDouble(input);

        if (isDeposit) {
            account.depositTransaction(amount, true);
        } else {
            try {
                account.withdrawTransaction(amount, true);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        updateBalanceInBankUsers(account, amount, isDeposit);
    }

    /**
     * Handles a transfer of funds from one account to another.
     *
     * @param fromAccount The account from which the funds will be transferred.
     */
    public void handleTransfer(Account fromAccount) {
        System.out.println("Enter recipient's ID:");
        String input = userInput.nextLine();
        if (input.equalsIgnoreCase(EXIT_COMMAND)) return;

        int recipientID = Integer.parseInt(input);

        if (!bankUsers.containsKey(recipientID)) {
            System.out.println("Recipient ID does not exist.");
            return;
        }

        System.out.println("Enter recipient account type (1: Checking, 2: Saving, 3: Credit):");
        String recipientAccountType = userInput.nextLine();
        if (recipientAccountType.equalsIgnoreCase(EXIT_COMMAND)) return;

        Account recipientAccount = RunBank.openAccount(recipientAccountType, bankUsers.get(recipientID), recipientID);

        if (recipientAccount == null) {
            System.out.println("Invalid recipient account type.");
            return;
        }

        System.out.println("Enter transfer amount:");
        input = userInput.nextLine();
        if (input.equalsIgnoreCase(EXIT_COMMAND)) return;

        double transferAmount = Double.parseDouble(input);

        try {
            fromAccount.transferTransaction(recipientAccount, transferAmount);
            updateBalanceInBankUsers(fromAccount, -transferAmount, false);
            updateBalanceInBankUsers(recipientAccount, transferAmount, true);
            System.out.println("Transfer successful.");
        } catch (Exception e) {
            System.out.println("Transfer failed: " + e.getMessage());
        }
    }

    /**
     * Updates the balance of a user's account in the system after a transaction (deposit/withdrawal/transfer).
     *
     * @param account   The account whose balance will be updated.
     * @param amount    The amount to be added or subtracted from the balance.
     * @param isDeposit If true, the amount is added to the balance; if false, it is subtracted.
     */
    @Override
    public void updateBalanceInBankUsers(Account account, double amount, boolean isDeposit) {
        double currentBalance = Double.parseDouble(bankUsers.get(account.getOwner().getCustomerID())[balanceIndex]);
        double updatedBalance = isDeposit ? currentBalance + amount : currentBalance - amount;

        String[] updatedUserInfo = bankUsers.get(account.getOwner().getCustomerID());
        updatedUserInfo[balanceIndex] = Double.toString(updatedBalance);
        bankUsers.put(account.getOwner().getCustomerID(), updatedUserInfo);
    }
}
