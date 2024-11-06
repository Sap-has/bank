public class CustomerOperations implements BankOperations{

    public Account openAccount(String accountType, String[] userInfo, int customerID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'openAccount'");
    }

    @Override
    public void performTransaction(Account account, double amount, boolean isDeposit) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'performTransaction'");
    }

    @Override
    public void transferFunds(Account fromAccount, Account toAccount, double amount) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'transferFunds'");
    }

    @Override
    public void updateBalanceInBankUsers(Account account, double amount, boolean isDeposit) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateBalanceInBankUsers'");
    }
    
}
