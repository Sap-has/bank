import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;

public class RunBank {
    public static void main(String[] args) throws IOException{
        HashMap<Integer, String[]> bankUsers = new HashMap<>();
        Scanner scnr = new Scanner(new File("C:\\Users\\Kayra Dominguez\\OneDrive\\Documents\\CS Codes\\CS4\\Bank\\bank\\info\\Bank Users.csv"));
        String firstLine = scnr.nextLine();
        while(scnr.hasNextLine()){
            String line = scnr.nextLine();
            String[] information = line.split(",");
            bankUsers.put(Integer.parseInt(information[0]), information);
        }
        scnr.close();
        System.out.println((bankUsers.get(79))[2]);
    }
}
