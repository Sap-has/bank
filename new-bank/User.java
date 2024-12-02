import java.util.HashMap;
import java.util.Scanner;

public class User {
    private final String EXIT_COMMAND = "exit";
    private final HashMap<Integer, Customer> bankUserCustomer = userBankHandler.bankUserCustomer;
    private final HashMap<Integer, String[]> bankUsers = userBankHandler.bankUsers;
    private final HashMap<String, Integer> bankuserNames = userBankHandler.bankUserNames;
    Scanner userInput = new Scanner(System.in);
    // handle new user creation
    // handle authentication
    // handle new password creation
    public Customer handleLogIn() {
        while (true) {
            System.out.println("To log in, provide either your full name (first name last name) or ID:");
            String input = userInput.nextLine();
            if (input.equalsIgnoreCase(EXIT_COMMAND)) return null;

            Customer customer = fetchCustomer(input);
            if (customer != null) {
                if (authenticateCustomer(customer)) {
                    System.out.println("Login successful!");
                    return customer;
                } else {
                    System.out.println("Incorrect password.");
                }
            } else {
                System.out.println("User not found. Please try again.");
            }
        }
    }

    public Customer fetchCustomer(String input) {
        if (input.matches("\\d+")) { // 1 or more digits
            int userId = Integer.parseInt(input);
            if (bankUsers.containsKey(userId)) {
                return bankUserCustomer.get(userId);
            }
        } else if (input.matches("[a-zA-Z]+\\s[a-zA-Z]+")) { // firstName(a-z) (space) lastName(a-z)
            if (bankuserNames.containsKey(input.toLowerCase())) {
                return bankUserCustomer.get(bankuserNames.get(input));
            }
        }
        return null;
    }

    private boolean authenticateCustomer(Customer customer) {
        System.out.println("Enter your password:");
        String password = userInput.nextLine();
        return customer.getPw().equals(password);
    }
}
