import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Scanner;

public class GenerateFiles {
    private static final HashMap<Integer, String[]> bankUsers = userBankHandler.bankUsers;
    private static final HashMap<Integer, String[]> initialBalances = userBankHandler.initialBalances;

    public void generateTransactionFiles(int customerId) {
        String[] userInfo = bankUsers.get(customerId);
        String firstName = userInfo[1];
        String lastName = userInfo[2];

        // Create the bankStatements directory if it doesn't exist
        File directory = new File("userTransactions");
        if (!directory.exists()) {
            directory.mkdirs();  // Creates the directory if it doesn't exist
        }

        // Construct the file path using the first name and last name
        String fileName = firstName + "_" + lastName + "-userTransaction.txt";
        File file = new File(directory, fileName);

        // Write to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            //Print account and user info
            writer.write("Customer Details:\n");
            writer.write("Name: " + firstName + " " + lastName + "\n");
            writer.write("Address: " + userInfo[4] + "\n");
            writer.write("Phone Number: " + userInfo[6] + "\n\n");

            writer.write("Accounts Details:\n");
            writer.write("Checking Account Number: " + userInfo[6] + "\n");
            writer.write("Checking Account Starting Balance: $" + initialBalances.get(customerId)[0] + "\n");
            writer.write("Checking Account Final Balance: $" + userInfo[7] + "\n\n");

            writer.write("Savings Account Number: " + userInfo[8] + "\n");
            writer.write("Savings Account Starting Balance: $" + initialBalances.get(customerId)[1] + "\n");
            writer.write("Savings Account Final Balance: $" + userInfo[9] + "\n\n");

            writer.write("Credit Account Number: " + userInfo[10] + "\n");
            writer.write("Credit Account Max: $" + userInfo[11] + "\n");
            writer.write("Credit Account Starting Balance: $" + initialBalances.get(customerId)[2] + "\n");
            writer.write("Credit Account Final Balance: $" + userInfo[12] + "\n\n");

            writer.write("Date of Statement: " + LocalDate.now() + "\n\n");

            writer.write("Date\t\tDescription\tAccount\t\tWithdrawal\tDeposit\t\tBalance\n");

            //Print transactions
            try (Scanner readLog = new Scanner(new File("new-bank\\info\\log.txt"))) {
                
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
}
