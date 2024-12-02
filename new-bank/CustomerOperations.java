import java.util.Scanner;

public class CustomerOperations implements BankOperations {
    private final String EXIT_COMMAND = "exit";

    private AccountOperations accountOperations = new AccountOperations();

    private Customer customer;

    Scanner userInput = new Scanner(System.in);

    @Override
    public void handleAccess() {
        if (callToLogIn() == null) return;
        while(true) {
            handleOperations();
            System.out.println("To continue doing more transaction press enter, else type exit to exit");
            String input = userInput.nextLine();
            if(input.equalsIgnoreCase(EXIT_COMMAND)) return;
        }
    }

    public Customer callToLogIn() {
        User user = new User();
        customer = user.handleLogIn();
        if (customer == null) return null;
        return customer;
    }

    public void handleOperations() {
        String operation = selectAccountOperation();
        if(operation.equalsIgnoreCase(EXIT_COMMAND)) return;
        while(operation.equalsIgnoreCase("-1")) {
            System.out.println("Invalid operation");
            operation = selectAccountOperation();
        }
        if(operation.equalsIgnoreCase(EXIT_COMMAND)) return;
        if(operation.equalsIgnoreCase("generate transaction file")) {
            GenerateFiles files = new GenerateFiles();
            files.generateTransactionFiles(customer.getCustomerID());
            return;
        }
        Account account = openAccount();
        if(account == null) return;
        handleTransaction(operation, account);
    }

    @Override
    public String selectAccountOperation() {
        System.out.println("What operation would you like to perform today");
        System.out.println("(1)Inquire Balance\n(2)Deposit\n(3)Withdraw\n(4)Transfer\n(5)Generate Transaction File");
        String input = userInput.nextLine();
        return accountOperations.selectOperation(input);
    }

    public Account openAccount() {
        System.out.println("Which account would you like to use?");
        System.out.println("(1)Checking\n(2)Saving\n(3)Credit");
        String input = userInput.nextLine();
        while(!input.equalsIgnoreCase("1") && !input.equalsIgnoreCase("2") && !input.equalsIgnoreCase("3")) {
            if(input.equalsIgnoreCase(EXIT_COMMAND)) return null;
            System.out.println("Not a valid account");
            System.out.println("Which account would you like to use today?");
            System.out.println("(1)Checking\n(2)Saving\n(3)Credit");
            input = userInput.nextLine();
        }
        return accountOperations.openAccount(input, customer);
    }

    public void handleTransaction(String op, Account acc) {
        op = op.toLowerCase();
        if(op.equals("inquire balance")) {
            accountOperations.performSingleOperation("1", acc, 0);
        } else if (op.equals("deposit")) {
            System.out.println("How much money do you want to deposit?");
            double amount = getAmount();
            while (amount == -1) {
                System.out.println("How much money do you want to deposit?");
                amount = getAmount();
            }
            accountOperations.performSingleOperation("2", acc, amount);
        } else if (op.equals("withdraw")) {
            System.out.println("How much money do you want to withdraw?");
            double amount = getAmount();
            while (amount == -1) {
                System.out.println("How much money do you want to withdraw?");
                amount = getAmount();
            }
            accountOperations.performSingleOperation("3", acc, amount);
        } else if (op.equals("transfer")) {
            System.out.println("How much money do you want to transfer?");
            double amount = getAmount();
            while (amount == -1) {
                System.out.println("How much money do you want to transfer?");
                amount = getAmount();
            }
            System.out.println("Who will you be transferring to?");
            String input = userInput.nextLine();
            User toUser = new User();
            while(toUser.fetchCustomer(input) == null) {
                System.out.println("User not found. Please try again.");
                System.out.println("Who will you be transferring to?");
                input = userInput.nextLine();
            }
            Account toAcc = openAccount();
            while(toAcc.getAccountNumber() == acc.getAccountNumber()) {
                System.out.println("Cannot transfer to same account!");
                System.out.println("Enter recipient account type (1: Checking, 2: Saving, 3: Credit):");
                toAcc = openAccount();
    
                if (toAcc == null) {
                    System.out.println("Invalid recipient account type.");
                    return;
                }
            }
            accountOperations.handleTransfer(acc, toAcc, amount);
        } else {
            System.out.println("Not working");
        }
    }

    public double getAmount() {
        double amount = 0;
        String input = userInput.nextLine();
        try {
            amount = Double.parseDouble(input);

            if (amount > 0) {
                return amount; // Exit the loop if input is valid
            } else {
                System.out.println("The amount must be a positive number. Please try again.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        }
        return -1;
    }
    
}
