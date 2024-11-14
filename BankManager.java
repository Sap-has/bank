import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

/**
 * This class implements the BankOperations interface and is responsible for managing
 * customer accounts and operations in the banking system. It provides functionality
 * for bank managers to access customer accounts, generate bank statements, and perform
 * operations such as deposits, withdrawals, and transfers (though not directly in this example).
 */
public class BankManager implements BankOperations {

    private static final HashMap<Integer, String[]> bankUsers = RunBank.bankUsers; // Reference from RunBank for simplicity
    private static final HashMap<String, Integer> bankUserNames = RunBank.bankUserNames;
    private static final String EXIT_COMMAND = "exit";
    private static int balanceIndex;
    private final static String TRANSACTION_PATH = "info\\Transactions.csv";
    private Scanner userInput = new Scanner(System.in);

    /**
     * Handles user access to the system. Allows a bank manager to enter a customer ID and perform
     * actions such as generating bank statements. The manager can also exit at any time.
     */
    public void handleUserAccess() {
        while (true) {
            System.out.println("Enter the customer ID or Full Name (or type 'exit' to exit):");
            String input = userInput.nextLine();
            int customerId;
            if (input.equalsIgnoreCase(EXIT_COMMAND)) return;
            if (input.split(" ").length == 2){
                //find customer id with full name in hashmap and make input the id
                customerId = bankUserNames.get(input);
            }else{
                customerId = Integer.parseInt(input);
            }

            if (!bankUsers.containsKey(customerId)) {
            System.out.println("ID does not exist.");
            return;
        }

            System.out.println("Account Information");
            displayCustomerDetailsForManager(customerId);

            System.out.println("\nGenerate a Bank Statement for Customer?");
            System.out.println("1. Yes");
            System.out.println("2. No");

            int answer = Integer.parseInt(userInput.nextLine());
            while (answer != 1 && answer != 2){
                System.out.println("Invalid Input. Put 1 or 2!");
                answer = Integer.parseInt(userInput.nextLine());
            }

            if (answer == 1) {
                try {
                    generateBankStatement(customerId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                return;  // Exit handleUserAccess method
            }
        }
    }

    /**
     * Displays the details of a customer for the bank manager, including account numbers,
     * account balances, and personal information.
     *
     * @param customerId The ID of the customer whose details are to be displayed.
     */
    private void displayCustomerDetailsForManager(int customerId) {
        String[] userInfo = bankUsers.get(customerId);
        System.out.println("Customer Details:");
        System.out.println("Name: " + userInfo[1] + " " + userInfo[2]);
        System.out.println("Checking Account Number: " + userInfo[6]);
        System.out.println("Checking Account Balance: $" + userInfo[7]);
        System.out.println("Savings Account Number: " + userInfo[8]);
        System.out.println("Savings Account Balance: $" + userInfo[9]);
        System.out.println("Credit Account Number: " + userInfo[10]);
        System.out.println("Credit Account Max: $" + userInfo[11]);
        System.out.println("Credit Account Balance: $" + userInfo[12]);
    }

    /**
     * Generates a bank statement for the specified customer, including details such as
     * customer information, account balances, and transaction logs. The bank statement
     * is written to a text file in the "bankStatements" directory.
     *
     * @param customerId The ID of the customer for whom the bank statement is being generated.
     * @throws Exception If an error occurs while generating the bank statement or accessing the log file.
     */
    private void generateBankStatement(int customerId) throws Exception {
        String[] userInfo = bankUsers.get(customerId);
        String firstName = userInfo[1];
        String lastName = userInfo[2];

        // Create the bankStatements directory if it doesn't exist
        File directory = new File("bankStatements");
        if (!directory.exists()) {
            directory.mkdirs();  // Creates the directory if it doesn't exist
        }

        // Construct the file path using the first name and last name
        String fileName = firstName + "_" + lastName + "-bankStatement.txt";
        File file = new File(directory, fileName);

        // Write to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("Customer Details:\n");
            writer.write("Name: " + firstName + " " + lastName + "\n");
            writer.write("Address: " + userInfo[4] + "\n");
            writer.write("Phone Number: " + userInfo[6] + "\n\n");

            writer.write("Accounts Details:\n");
            writer.write("Checking Account Number: " + userInfo[6] + "\n");
            writer.write("Checking Account Balance: $" + userInfo[7] + "\n\n");

            writer.write("Savings Account Number: " + userInfo[8] + "\n");
            writer.write("Savings Account Balance: $" + userInfo[9] + "\n\n");

            writer.write("Credit Account Number: " + userInfo[10] + "\n");
            writer.write("Credit Account Max: $" + userInfo[11] + "\n");
            writer.write("Credit Account Balance: $" + userInfo[12] + "\n\n");

            writer.write("Date\t\tDescription\tAccount\t\tWithdrawal\tDeposit\t\tBalance\n");

            try (Scanner readLog = new Scanner(new File("info\\log.txt"))) {
                while (readLog.hasNextLine()) {
                    String[] logLine = readLog.nextLine().split(" ");
                    // Checking if the log matches the customer
                    if (logLine[0].equals(firstName) && logLine[1].equals(lastName)) {
                        switch (logLine[2]) {
                            case "deposited":
                                writer.write(LocalDate.now() + "\t" + "Deposit" + "\t\t" + logLine[6] + "\t\t____\t\t" + logLine[3] + "\t\t" + logLine[9] + "\n");
                                break;
                            case "withdrew":
                                writer.write(LocalDate.now() + "\t" + "Withdraw" + "\t" + logLine[6] + "\t\t" + logLine[3] + "\t\t____\t\t" + logLine[9] + "\n");
                                break;
                            case "transferred":
                                writer.write(LocalDate.now() + "\t" + "Transfer" + "\t" + logLine[6] + "\t\t" + logLine[3] + "\t\t____\t\t" + logLine[15] + "\n");
                                break;
                        }
                    } else if (logLine.length > 7 && logLine[8].equals(firstName) && (logLine[9].split("\\'"))[0].equals(lastName)) {
                        writer.write(LocalDate.now() + "\t" + "Transfer" + "\t" + logLine[11] + "\t\t____\t\t" + logLine[3] + "\t\t" + logLine[19] + "\n");
                    }
                }
            } catch (FileNotFoundException e) {
                // Handle file not found exception if the log file doesn't exist
                System.err.println("Error reading the log file: " + e.getMessage());
                e.printStackTrace();
            }

        } catch (IOException e) {
            System.err.println("Error writing to the file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void readTransactionFile() {
        try(Scanner readTransaction = new Scanner(new File(TRANSACTION_PATH))) {
            while(readTransaction.hasNextLine()) {
                String[] transactionOperation = readTransaction.nextLine().split(",");
                System.out.println(Arrays.toString(transactionOperation));
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error reading the log file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles a transaction for the specified account. In this example, managers are not allowed
     * to perform transactions directly.
     *
     * @param account The account on which the transaction would occur.
     * @param isDeposit Whether the transaction is a deposit (true) or withdrawal (false).
     */
    @Override
    public void handleTransaction(Account account, boolean isDeposit) {
        // Manager may not handle direct transactions in this example.
        System.out.println("Managers are not permitted to perform this transaction.");
    }

    /**
     * Handles a transfer between accounts. In this example, managers are not allowed to perform
     * transfers directly.
     *
     * @param fromAccount The account from which the transfer would be made.
     */
    @Override
    public void handleTransfer(Account fromAccount) {
        // Manager may not handle direct transfers in this example.
        System.out.println("Managers are not permitted to perform transfers.");
    }

    /**
     * Updates the balance of a given account in the bank users data.
     *
     * @param account The account whose balance is being updated.
     * @param amount The amount to be added or subtracted from the account balance.
     * @param isDeposit Indicates whether the operation is a deposit (true) or withdrawal (false).
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
