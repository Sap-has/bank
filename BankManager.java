import java.util.HashMap;
import java.util.Scanner;

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
    }


    @Override
    public void selectAccountAndPerformOperations(int customerId) {
        String[] userInfo = bankUsers.get(customerId);

        System.out.println("Select an account to access:");
        System.out.println("1. Checking");
        System.out.println("2. Saving");
        System.out.println("3. Credit");

        String accountType = userInput.nextLine();
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
