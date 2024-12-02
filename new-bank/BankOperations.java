public interface BankOperations {
    public abstract void handleAccess();
    public abstract String selectAccountOperation();
    public abstract Account openAccount();
}
