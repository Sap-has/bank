import java.util.HashMap;
import java.util.Scanner;

public class CustomerOperation implements BankOperations {
    private static final HashMap<Integer, String[]> bankUsers = RunBank.bankUsers; // Reference from RunBank for simplicity
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

        System.out.println("Please give your First Name");
        String firstName = userInput.nextLine();

        System.out.println("Last Name");
        String lastName = userInput.nextLine();

        System.out.println("Date of Birth (Day-Month-Year)");
        String dateOfBirth = userInput.nextLine();

        System.out.println("Address (Street, City, State Zipcode)");
        String address = userInput.nextLine();

        System.out.println("Phone Number");
        String phoneNum = userInput.nextLine();

        Customer newCustomer = new Customer(firstName, lastName, dateOfBirth, address, phoneNum, bankUsers.size());
        
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
