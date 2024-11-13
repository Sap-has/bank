interface BankOperations {
    // attributes public static final
    abstract public void handleUserAccess();
    abstract public void handleTransaction(Account account, boolean isDeposit);
    abstract public void handleTransfer(Account fromAccount);
    abstract public void updateBalanceInBankUsers(Account account, double amount, boolean isDeposit);
    
}