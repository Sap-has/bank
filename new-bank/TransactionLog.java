import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The {@code TransactionLog} class provides a mechanism for logging 
 * transaction entries and saving them to a file. It maintains a list 
 * of log entries that can be added, retrieved, and saved to an external 
 * file for persistent storage.
 * 
 * @author Epifanio Sarinana
 * @author Kayra 
 */
public class TransactionLog {
    private ArrayList<String> logEntries = new ArrayList<>();
    final String LOG_FILE = "new-bank\\info\\log.txt";

    /**
     * Adds a new transaction entry to the log.
     * The entry is also printed to the console.
     * 
     * @param entry the transaction to log
     */
    public void logTransaction(String entry) {
        logEntries.add(entry);
        System.out.println(entry);
    }

    /**
     * Returns the list of transaction entries currently in the log.
     * 
     * @return an {@code ArrayList} containing all log entries
     */
    public ArrayList<String> getLogEntries() {
        return logEntries;
    }

    /**
     * Saves all log entries to a file specified by {@code LOG_FILE}.
     * Appends each entry to the file, and clears the log entries list
     * after saving. If an I/O error occurs, an error message is printed
     * to the console.
     */
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