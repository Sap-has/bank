import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CustomerOperation implements BankOperations {
    private static final HashMap<Integer, String[]> bankUsers = RunBank.bankUsers; // Reference from RunBank for simplicity
    private static final Map<String, Integer> headerIndexMap = RunBank.headerIndexMap;
    private static final String CSV_FILE_PATH = "info\\Bank Users.csv";
    private static final String EXIT_COMMAND = "exit";
    private static int balanceIndex;
    Scanner userInput = new Scanner(System.in);

    public void handleUserAccess() {
        isNewUser();
        System.out.println("Enter the customer ID:");
        String input = userInput.nextLine();
        if (input.equalsIgnoreCase(EXIT_COMMAND)) return;

        int customerId = Integer.parseInt(input);

        if (!bankUsers.containsKey(customerId)) {
            System.out.println("ID does not exist.");
            return;
        }
        selectAccountAndPerformOperations(customerId);
    }

    public void isNewUser() {
        System.out.println("Are you a (1) customer with us or (2) do you want to set up an account?");
        String newUser = userInput.nextLine();
        if("1".equalsIgnoreCase(newUser)) return;
        
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

        Customer newCustomer = new Customer(firstName, lastName, dateOfBirth, address, phoneNum, bankUsers.size()+1);
        addNewUser(newCustomer);
    }

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

    private void appendNewUserToCSV(String[] userInfo) {
        try (FileWriter writer = new FileWriter(CSV_FILE_PATH, true)) {
            writer.write(String.join(",", userInfo) + "\n");
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
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

    @Override
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

    @Override
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

    @Override
    public void updateBalanceInBankUsers(Account account, double amount, boolean isDeposit) {
        double currentBalance = Double.parseDouble(bankUsers.get(account.getOwner().getCustomerID())[balanceIndex]);
        double updatedBalance = isDeposit ? currentBalance + amount : currentBalance - amount;

        String[] updatedUserInfo = bankUsers.get(account.getOwner().getCustomerID());
        updatedUserInfo[balanceIndex] = Double.toString(updatedBalance);
        bankUsers.put(account.getOwner().getCustomerID(), updatedUserInfo);
    }
}
