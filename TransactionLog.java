import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TransactionLog {
    private static ArrayList<String> logEntries = new ArrayList<>();
    private static final String LOG_FILE = "Log.txt";

    public static void logTransaction(String entry) {
        logEntries.add(entry);
    }

    public static ArrayList<String> getLogEntries() {
        return logEntries;
    }

    public static void saveLog() {
        // TODO Implement saving text to log.txt and catch an error
    }
}

