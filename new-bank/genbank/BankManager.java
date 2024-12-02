package genbank;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class BankManager implements BankOperations {
    Scanner userInput = new Scanner(System.in);
    private final HashMap<String, Integer> bankUserNames = userBankHandler.bankUserNames;
    private final HashMap<Integer, Customer> bankUserCustomer = userBankHandler.bankUserCustomer;
    GenerateFiles files = new GenerateFiles();
    private final String TRANSACTION_PATH = "new-bank\\info\\Transactions.csv";

    AccountOperations accountOperations = new AccountOperations();
    User user = new User();

    Customer customer;
    private final String EXIT_COMMAND = "exit";

    @Override
    public void handleAccess() {
        while(true) {
            String opString = selectAccountOperation();
            if(opString == null) return;
            try {
                handleOperations(opString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("To continue doing more transaction press enter, else type exit to exit");
            String input = userInput.nextLine();
            if(input.equalsIgnoreCase(EXIT_COMMAND)) return;
        }

    }

    @Override
    public String selectAccountOperation() {
        System.out.println("What would you like to do today?");
        String operation = "";
        System.out.println("(1)Inquire Balance\n(2)Read Transaction File\n(3)Generate Bank Statement\n(4)Add new User");
        operation = userInput.nextLine();
        if(operation.equalsIgnoreCase(EXIT_COMMAND)) return null;
        while(!operation.equalsIgnoreCase("1") && !operation.equalsIgnoreCase("2") && !operation.equalsIgnoreCase("3") && !operation.equalsIgnoreCase("4")) {
            if(operation.equalsIgnoreCase(EXIT_COMMAND)) return null;
            System.out.println("Not a valid operation");
            System.out.println("What would you like to do today?");
            System.out.println("(1)Inquire Balance\n(2)Read Transaction File\n(3)Generate Bank Statement\n(4)Add new User");
            operation = userInput.nextLine();
        }
        return operation;
    }

    public void handleOperations(String op) throws Exception {
        if(op.equalsIgnoreCase("1") || op.equalsIgnoreCase("3")) fetchCustomer();

        if(op.equalsIgnoreCase("1")) accountOperations.displayCustomerDetailsForManager(customer.getCustomerID());
        if(op.equalsIgnoreCase("2")) transactionReader();
        if(op.equalsIgnoreCase("3")) files.generateBankStatement(customer.getCustomerID());
        if(op.equalsIgnoreCase("4")) user.handleNewUser();
    }

    public void fetchCustomer() {
        System.out.println("Please give user by name or ID");
        String input = userInput.nextLine();
        customer = user.fetchCustomer(input);
    }

    public void transactionReader() {
        try (BufferedReader br = new BufferedReader(new FileReader(TRANSACTION_PATH))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                processTransaction(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading transactions file: " + e.getMessage());
        }
    }

    private void processTransaction(String transaction) {
        String[] parts = transaction.split(",");
        String fromFirstName = parts[0];
        String fromLastName = parts[1];
        String fromAccountType = parts[2];
        String action = parts[3];
        String toFirstName = parts.length > 4 ? parts[4]: "";
        String toLastName = parts.length > 5 ? parts[5]: "";
        String toAccountType = parts.length > 6 ? parts[6]: "";
        double amount = parts.length > 7 ? Double.parseDouble(parts[7]) : -1;

        Account fromAccount = null;
        Account toAccount = null;

        if(fromAccountType.equals("Checking")) fromAccountType = "1";
        else if(fromAccountType.equals("Savings")) fromAccountType = "2";
        else fromAccountType = "3";

        if (!fromFirstName.isEmpty() && !fromLastName.isEmpty()) {
            int fromUserId = getUserId(fromFirstName, fromLastName);
            fromAccount = accountOperations.openAccount(fromAccountType, bankUserCustomer.get(fromUserId));
    
            if (fromAccount == null) {
                System.out.println("Error: 'From' account not found for " + fromFirstName + " " + fromLastName);
                return;
            }
        }
    
        if (!toFirstName.isEmpty() && !toLastName.isEmpty()) {
            int toUserId = getUserId(toFirstName, toLastName);
            toAccount = accountOperations.openAccount(toAccountType, bankUserCustomer.get(toUserId));
    
            if (toAccount == null) {
                System.out.println("Error: 'To' account not found for " + toFirstName + " " + toLastName);
                return;
            }
        }

        switch (action) {
            case "pays":
                if (fromAccount != null && toAccount != null) {
                    handlePay(fromAccount, toAccount, amount);
                } else {
                    System.out.println("Payment failed: Missing 'from' or 'to' account information.");
                }
                break;
            case "transfers":
                if (fromAccount != null) {
                    accountOperations.handleTransfer(fromAccount, toAccount, amount);
                } else {
                    System.out.println("Transfer failed: Missing 'from' account information.");
                }
                break;
            case "inquires":
                if (fromAccount != null) {
                    handleInquiry(fromAccount);
                } else {
                    System.out.println("Inquiry failed: Missing 'from' account information.");
                }
                break;
            case "withdraws":
                if (fromAccount != null) {
                    accountOperations.handleTransaction(fromAccount, false, amount); // Withdrawal
                } else {
                    System.out.println("Withdrawal failed: Missing 'from' account information.");
                }
                break;
            case "deposits":
                if (toAccount != null) {
                    accountOperations.handleTransaction(toAccount, true, amount); // Deposit
                } else {
                    System.out.println("Deposit failed: Missing 'to' account information.");
                }
                break;
            default:
                System.out.println("Unknown transaction type: " + action);
                break;
        }
    }

    /**
     * Handles paying accounts from the Transaction Reader
     * @param fromAccount
     * @param toAccount
     * @param amount
     */
    private void handlePay(Account fromAccount, Account toAccount, double amount) {
        accountOperations.handleTransfer(fromAccount, toAccount, amount);
    }

    /**
     * Hanldes doing an inquiry from the Transaction reader
     * @param account
     */
    private void handleInquiry(Account account) {
        System.out.println("Account inquiry for " + account.getOwner().getName() + " on " + account.getAccountNumber());
        System.out.println("Current balance: " + account.getBalance());
    }

    /**
     * Returns id using first and last name of user
     * @param firstName
     * @param lastName
     * @return
     */
    private int getUserId(String firstName, String lastName) {
        return bankUserNames.get(firstName + " " + lastName);
    }
    
}
