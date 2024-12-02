import java.util.HashMap;
import java.util.Scanner;

public class AccountOperations {
    Scanner userInput = new Scanner(System.in);
    private int balanceIndex;

    private static final HashMap<Integer, String[]> bankUsers = userBankHandler.bankUsers; // Reference from RunBank for simplicity

    public String selectOperation(String input) {
        input.toLowerCase();
        switch (input) {
            case "1":
                return "Inquire Balance";
            case "2":
                return "Deposit";
            case "3":
                return "Withdraw";
            case "4":
                return "Transfer";
            case "5":
                return "Generate Transaction file";
            case "exit":
                return "exit";
            default:
                return "-1";
        }
    }

    public void performSingleOperation(String operation, Account account, double amount) {
        operation.toLowerCase();
        switch (operation) {
            case "1":
                account.inquireBalance();
                break;
            case "2":
                handleTransaction(account, true, amount);
                break;
            case "3":
                handleTransaction(account, false, amount);
                break;
            default:
                break;
        }
    }

    public void handleTransaction(Account account, boolean isDeposit, double amount) {
        if (isDeposit) {
            account.depositTransaction(amount, true);
            updateBalanceInBankUsers(account, amount, isDeposit);
        } else {
            try {
                account.withdrawTransaction(amount, true);
                updateBalanceInBankUsers(account, amount, isDeposit);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public void handleTransfer(Account fromAccount, Account toAccount, double amount) {
        try {
            fromAccount.transferTransaction(toAccount, amount);
            updateBalanceInBankUsers(fromAccount, -amount, false);
            updateBalanceInBankUsers(toAccount, amount, true);
            System.out.println("Transfer successful.");
        } catch (Exception e) {
            System.out.println("Transfer failed: " + e.getMessage());
        }
    }


    public double updateBalanceInBankUsers(Account account, double amount, boolean isDeposit) {
        double currentBalance = Double.parseDouble(bankUsers.get(account.getOwner().getCustomerID())[balanceIndex]);
        System.out.println(Double.parseDouble(bankUsers.get(account.getOwner().getCustomerID())[balanceIndex]) + "In updatade Balance");
        double updatedBalance = isDeposit ? currentBalance + amount : currentBalance - amount;

        String[] updatedUserInfo = bankUsers.get(account.getOwner().getCustomerID());
        updatedUserInfo[balanceIndex] = Double.toString(updatedBalance);
        bankUsers.put(account.getOwner().getCustomerID(), updatedUserInfo);
        System.out.println(Double.parseDouble(bankUsers.get(account.getOwner().getCustomerID())[balanceIndex]) + " updataded Balance");

        return Double.parseDouble(bankUsers.get(account.getOwner().getCustomerID())[balanceIndex]);
    }

    public Account openAccount(String account, Customer customer) {
        switch (account) {
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
        return customer.openAccount(account);
    }
}
