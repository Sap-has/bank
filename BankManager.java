public class BankManager implements BankOperations {
    private final String TRANSACTION_CSV_FILE_PATH = "info/Transactions.csv";

    public void generateBankStatement(Account account) {

    }

    public void completeTransactions() {

    }

    public void displayCustomerDetailsForManager(int customerID) {

    }

    @Override
    public void performTransaction(Account account, double amount, boolean isDeposit) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'performTransaction'");
    }

    @Override
    public void transferFunds(Account fromAccount, Account toAccount, double amount) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'performTransaction'");
    }

    @Override
    public void updateBalanceInBankUsers(Account account, double amount, boolean isDeposit) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'performTransaction'");
    }

    
}
