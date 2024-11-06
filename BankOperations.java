interface BankOperations {
    // attributes public static final
    abstract public void performTransactio(Account account, double amount, boolean isDeposit);
    abstract public void transferFunds(Account fromAccount, Account toAccount, double amount);
    abstract public void updateBalanceInBankUsers(Account account, double amount, boolean isDeposit);
}