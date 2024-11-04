import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;

// TODO update all diagrams
// TODO improve the log transactions for all methods

// get length of hashmap - will be id of new customer


public class RunBank {
    private static final HashMap<Integer, String[]> bankUsers = new HashMap<>();
    private static final String CSV_FILE_PATH = "info\\Bank Users.csv";
    private static String CSV_HEADER = "";
    private static final String EXIT_COMMAND = "exit";
    private static final String USER_OPTION = "1";
    private static final String MANAGER_OPTION = "2";
    private static final String CHECKING_OPTION = "1";
    private static final String SAVING_OPTION = "2";
    private static final String CREDIT_OPTION = "3";
    private static final double OVERDRAFT_LIMIT = 500;
    private static final double INTERESR_RATE = 0.02;
    private static final double CREDITPRINCIPLE = 0;
    private static int balanceIndex;
    private static final String NEW_CSV_FILE_PATH = "info\\New Bank Users.csv";

    public static void main(String[] args) throws IOException {
        loadBankUsersFromCSV();

        Scanner userInput = new Scanner(System.in);
        System.out.println("Welcome to the Bank System. Enter 'exit' to exit the program.");

        while (true) {
            System.out.println("Are you a User or Bank Manager?");
            System.out.println("1. User");
            System.out.println("2. Bank Manager");

            String userSelection = userInput.nextLine();
            if (userSelection.equalsIgnoreCase(EXIT_COMMAND)) break;

            switch (userSelection) {
                case USER_OPTION:
                    handleUserAccess(userInput, false);
                    break;
                case MANAGER_OPTION:
                    handleUserAccess(userInput, true);
                    break;
                default:
                    System.out.println("Invalid option. Please choose correctly.");
            }
        }

        userInput.close();
        updateBankUsersInformation();
        System.out.println("Thank you for using the Bank System.");
    }

    private static void loadBankUsersFromCSV() throws IOException {
        Scanner bankFileScanner = new Scanner(new File(CSV_FILE_PATH));
        CSV_HEADER = bankFileScanner.nextLine(); // Skip header

        while (bankFileScanner.hasNextLine()) {
            String[] userInfo = parseCSVLine(bankFileScanner.nextLine());
            bankUsers.put(Integer.parseInt(userInfo[0]), userInfo);
        }

        bankFileScanner.close();
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

    private static String[] parseCSVLine(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    }

    private static void handleUserAccess(Scanner userInput, boolean isManager) {
        System.out.println("Enter the customer ID:");
        String input = userInput.nextLine();
        if (input.equalsIgnoreCase(EXIT_COMMAND)) return;

        int customerId = Integer.parseInt(input);

        if (!bankUsers.containsKey(customerId)) {
            System.out.println("ID does not exist.");
            return;
        }

        if (isManager) {
            System.out.println("Account Information");
            displayCustomerDetailsForManager(customerId);
        } else {
            selectAccountAndPerformOperations(customerId, userInput);
        }
    }

    private static void selectAccountAndPerformOperations(int customerId, Scanner userInput) {
        String[] userInfo = bankUsers.get(customerId);

        System.out.println("Select an account to access:");
        System.out.println("1. Checking");
        System.out.println("2. Saving");
        System.out.println("3. Credit");

        String accountType = userInput.nextLine();
        if (accountType.equalsIgnoreCase(EXIT_COMMAND)) return;

        Account account = openAccount(accountType, userInfo, customerId);

        if (account != null) {
            performAccountOperations(account, userInput);
        } else {
            System.out.println("Invalid account type.");
        }
    }

    private static Account openAccount(String accountType, String[] userInfo, int customerId) {
        Customer customer = new Customer(userInfo[1] + " " + userInfo[2], userInfo[4], customerId);

        switch (accountType) {
            case CHECKING_OPTION:
                balanceIndex = 7;
                return new Checking(userInfo[6], customer, Double.parseDouble(userInfo[7]), OVERDRAFT_LIMIT);
            case SAVING_OPTION:
                balanceIndex = 9;
                return new Saving(userInfo[8], customer, Double.parseDouble(userInfo[9]), INTERESR_RATE);
            case CREDIT_OPTION:
                balanceIndex = 12;
                return new Credit(userInfo[10], customer, Double.parseDouble(userInfo[12]), Double.parseDouble(userInfo[11]), CREDITPRINCIPLE);
            default:
                return null;
        }
    }

    private static void performAccountOperations(Account account, Scanner userInput) {
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
                handleTransaction(account, userInput, true);
                break;
            case "3":
                handleTransaction(account, userInput, false);
                break;
            case "4":
                handleTransfer(account, userInput);
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private static void handleTransaction(Account account, Scanner userInput, boolean isDeposit) {
        System.out.println("Enter amount:");
        String input = userInput.nextLine();
        if (input.equalsIgnoreCase(EXIT_COMMAND)) return;

        double amount = Double.parseDouble(input);

        if (isDeposit) {
            account.deposit(amount, true);
        } else {
            try {
                account.withdraw(amount, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        updateBalanceInBankUsers(account, amount, isDeposit);
        account.inquireBalance();
    }

    private static void handleTransfer(Account fromAccount, Scanner userInput) {
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
    
        Account recipientAccount = openAccount(recipientAccountType, bankUsers.get(recipientID), recipientID);
    
        if (recipientAccount == null) {
            System.out.println("Invalid recipient account type.");
            return;
        }
    
        System.out.println("Enter transfer amount:");
        input = userInput.nextLine();
        if (input.equalsIgnoreCase(EXIT_COMMAND)) return;
    
        double transferAmount = Double.parseDouble(input);
    
        try {
            fromAccount.transfer(recipientAccount, transferAmount);
            updateBalanceInBankUsers(fromAccount, -transferAmount, false);
            updateBalanceInBankUsers(recipientAccount, transferAmount, true);
            System.out.println("Transfer successful.");
        } catch (Exception e) {
            System.out.println("Transfer failed: " + e.getMessage());
        }
    }
    

    private static void updateBalanceInBankUsers(Account account, double amount, boolean isDeposit) {
        double currentBalance = Double.parseDouble(bankUsers.get(account.getOwner().getCustomerID())[balanceIndex]);
        double updatedBalance = isDeposit ? currentBalance + amount : currentBalance - amount;

        String[] updatedUserInfo = bankUsers.get(account.getOwner().getCustomerID());
        updatedUserInfo[balanceIndex] = Double.toString(updatedBalance);
        bankUsers.put(account.getOwner().getCustomerID(), updatedUserInfo);
    }

    private static void displayCustomerDetailsForManager(int customerId) {
        String[] userInfo = bankUsers.get(customerId);
        
        // Display customer name
        System.out.println("Customer Details:");
        System.out.println("Name: " + userInfo[1] + " " + userInfo[2]);
        
        // Display Checking account details
        System.out.println("Checking Account Number: " + userInfo[6]);
        System.out.println("Checking Account Balance: $" + userInfo[7]);
        
        // Display Savings account details
        System.out.println("Savings Account Number: " + userInfo[8]);
        System.out.println("Savings Account Balance: $" + userInfo[9]);
        
        // Display Credit account details
        System.out.println("Credit Account Number: " + userInfo[10]);
        System.out.println("Credit Account Max: $" + userInfo[11]);
        System.out.println("Credit Account Balance: $" + userInfo[12]);
    }
}
