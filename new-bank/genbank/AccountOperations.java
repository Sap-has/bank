package genbank;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class AccountOperations {
    Scanner userInput = new Scanner(System.in);
    private int balanceIndex;

    public HashMap<Integer, String[]> bankUsers = userBankHandler.bankUsers; // Reference from RunBank for simplicity

    public double calculateCreditLimit(int creditScore) {
        double creditLimit = 0.0;
        // got these values from the table of creditScores and Credit Limit
        if(creditScore <= 580) creditLimit = new Random().nextInt(600) + 100;
        if(creditScore <= 669) creditLimit = new Random().nextInt(4300) + 700;
        if(creditScore <= 739) creditLimit = new Random().nextInt(2500) + 5000;
        if(creditScore <= 799) creditLimit = new Random().nextInt(8500) + 7500;
        else creditLimit = new Random().nextInt(9001)+ 16000;
        return creditLimit;
    }

    public void displayCustomerDetailsForManager(int customerId) {
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
