interface BankOperations {
    // attributes public static final
    abstract public void handleUserAccess();
    abstract public void updateBalanceInBankUsers(Account account, double amount, boolean isDeposit);
    
}