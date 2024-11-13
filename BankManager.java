import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;


public class BankManager implements BankOperations {
    private static final HashMap<Integer, String[]> bankUsers = RunBank.bankUsers; // Reference from RunBank for simplicity
    private static final String EXIT_COMMAND = "exit";
    private static int balanceIndex;
    Scanner userInput = new Scanner(System.in);

    public void handleUserAccess() {
        System.out.println("Enter the customer ID:");
        String input = userInput.nextLine();
        if (input.equalsIgnoreCase(EXIT_COMMAND)) return;

        int customerId = Integer.parseInt(input);

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
        if (answer == 1){
            try {
                generateBankStatement(customerId);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (answer == 2){
            return;
        }
    }

    @Override
    public void selectAccountAndPerformOperations(int customerId) {
        String[] userInfo = bankUsers.get(customerId);

        System.out.println("Select an account to access:");
        System.out.println("1. Checking");
        System.out.println("2. Saving");
        System.out.println("3. Credit");

        String accountType = userInput.nextLine();
        if (accountType.equals("4")){
            try {
                generateBankStatement(customerId);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return;
        }
        if (accountType.equalsIgnoreCase(EXIT_COMMAND)) return;

        Account account = RunBank.openAccount(accountType, userInfo, customerId);

        if (account != null) {
            System.out.println("TODO");
        } else {
            System.out.println("Invalid account type.");
        }
    }

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

    private void generateBankStatement(int customerId) throws Exception{
        String[] userInfo = bankUsers.get(customerId);
        System.out.println("Customer Details:");
        System.out.println("Name: " + userInfo[1] + " " + userInfo[2]);
        System.out.println("Address: " + userInfo[4]);
        System.out.println("Phone Number: " + userInfo[6] + "\n");

        System.out.println("Accounts Details: ");
        System.out.println("Checking Account Number: " + userInfo[6]);
        System.out.println("Checking Account Balance: $" + userInfo[7] +"\n");

        System.out.println("Savings Account Number: " + userInfo[8]);
        System.out.println("Savings Account Balance: $" + userInfo[9] + "\n");

        System.out.println("Credit Account Number: " + userInfo[10]);
        System.out.println("Credit Account Max: $" + userInfo[11]);
        System.out.println("Credit Account Balance: $" + userInfo[12] + "\n");

        System.out.println("Date\t\tDescription\tAccount\t\tWithdrawal\tDeposit\t\tBalance");

        try {
            Scanner readLog = new Scanner(new File("info\\log.txt"));
            //Scanner readLog = new Scanner(new File("C:\\Users\\Kayra Dominguez\\OneDrive\\Documents\\CS Codes\\CS4\\Bank\\bank\\info\\log.txt"));
            while (readLog.hasNextLine()){
                String[] logLine = readLog.nextLine().split(" ");
                //Checking if wanted customer matches the log customer
                if(logLine[0].equals(bankUsers.get(customerId)[1]) && logLine[1].equals(bankUsers.get(customerId)[2])){
                    switch(logLine[2]){
                        case "deposited":
                            System.out.println(LocalDate.now()+"\t" + "Deposit" + "\t\t" + logLine[6] + "\t\t____\t\t" + logLine[3] + "\t\t" + logLine[9]);
                            break;
                        case "withdrew":
                            System.out.println(LocalDate.now()+"\t" + "Withdraw" + "\t" + logLine[6] + "\t\t" + logLine[3] + "\t\t____\t\t" + logLine[9]);
                            break;
                        case "transferred":
                            System.out.println(LocalDate.now()+"\t" + "Transfer" + "\t" + logLine[6] + "\t\t" + logLine[3] + "\t\t____\t\t" + logLine[9]);
                            break;
                    }  
                } else if (logLine.length > 7 && logLine[8].equals(bankUsers.get(customerId)[1]) && (logLine[9].split("\\'"))[0].equals(bankUsers.get(customerId)[2])){
                    System.out.println(LocalDate.now()+"\t" + "Transfer" + "\t" + logLine[11] + "\t\t____\t\t" + logLine[3] + "\t\t" + logLine[19]);
                }
            }
            readLog.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void handleTransaction(Account account, boolean isDeposit) {
        // Manager may not handle direct transactions in this example.
        System.out.println("Managers are not permitted to perform this transaction.");
    }

    @Override
    public void handleTransfer(Account fromAccount) {
        // Manager may not handle direct transfers in this example.
        System.out.println("Managers are not permitted to perform transfers.");
    }

    @Override
    public void updateBalanceInBankUsers(Account account, double amount, boolean isDeposit) {
        double currentBalance = Double.parseDouble(bankUsers.get(account.getOwner().getCustomerID())[balanceIndex]);
        double updatedBalance = isDeposit ? currentBalance + amount : currentBalance - amount;


        String[] updatedUserInfo = bankUsers.get(account.getOwner().getCustomerID());
        updatedUserInfo[balanceIndex] = Double.toString(updatedBalance);
        bankUsers.put(account.getOwner().getCustomerID(), updatedUserInfo);
    }
}