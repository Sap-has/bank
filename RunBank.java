import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;

public class RunBank {
    public static void main(String[] args) throws IOException{
        HashMap<Integer, String[]> bankUsers = new HashMap<>();
        Scanner bank_users = new Scanner(new File("info\\Bank Users.csv"));
        Scanner user_in = new Scanner(System.in);
        String user_inS;

        String firstLine = bank_users.nextLine(); // add to new bank users csv file
        while(bank_users.hasNextLine()){
            String line = bank_users.nextLine();
            String[] information = line.split(",");
            bankUsers.put(Integer.parseInt(information[0]), information);
        }
        bank_users.close();
        System.out.println((bankUsers.get(79))[2]);

        System.out.println("Welcome to the Bank System");
        System.out.println("Enter exit to exit program");
        
        do {
            // ask for id
            // check if its a valid id
            System.out.println("Inquire about account by inputing your ID: ");
            user_inS = user_in.nextLine().toUpperCase();
            if(bankUsers.get(Integer.parseInt(user_inS)) == null) {
                System.out.println("ID does not exist");
                continue;
            } else {
                System.out.println("Which account will you like to access?");
                System.out.println("1. Checking");
                System.out.println("2. Saving");
                System.out.println("3. Credit");

                user_inS = user_in.nextLine();
                while(!user_inS.equals("1") && !user_inS.equals("2") && !user_inS.equals("3")) {
                    switch (user_inS) {
                        case "1": // checking
                            System.out.println("1"); 
                            break;
                        case "2": // saving
                            System.out.println("2"); 
                            break;
                        case "3": // credit
                            System.out.println("3"); 
                            break;
                        default:
                            System.out.println("Pick a valid operation");
                            System.out.println("Which account will you like to access?");
                            System.out.println("1. Checking");
                            System.out.println("2. Saving");
                            System.out.println("3. Credit");
                            user_inS = user_in.nextLine();
                            break;
                    }
                }
            }



        } while (!"EXIT".equals(user_inS));

        user_in.close();
    }
}
