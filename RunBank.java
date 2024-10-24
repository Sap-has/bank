import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;

public class RunBank {
    public static void main(String[] args) throws IOException{
        HashMap<Integer, String[]> bankUsers = new HashMap<>();
        Scanner scnr = new Scanner(new File("info/Bank Users.csv"));
        while(scnr.hasNextLine()){
            String line = "";
            String[] information = line.split(",");
            bankUsers.put(Integer.parseInt(information[0]), information);
        }
        scnr.close();
        System.out.println(bankUsers);
    }
}
