import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TransactionLog {
    private ArrayList<String> logEntries = new ArrayList<>();
    private final String LOG_FILE = "info/log.txt";

    public void logTransaction(String entry) {
        logEntries.add(entry);
        System.out.println(entry);
    }

    public ArrayList<String> getLogEntries() {
        return logEntries;
    }

    public void saveLog() {
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            for (String entry : logEntries) {
                writer.write(entry + "\n");
            }
            logEntries.clear(); // Clear log after saving
        } catch (IOException e) {
            System.out.println("Error saving the transaction log: " + e.getMessage());
        }
    }
}
