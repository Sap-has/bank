import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TransactionLog {
    private ArrayList<String> logEntries = new ArrayList<>();
    private final String LOG_FILE = "Log.txt";

    public void logTransaction(String entry) {
        logEntries.add(entry);
    }

    public ArrayList<String> getLogEntries() {
        return logEntries;
    }

    public void saveLog() {
        // TODO Implement saving text to log.txt and catch an error
    }
}

