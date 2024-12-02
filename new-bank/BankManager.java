public class BankManager implements BankOperations {

    @Override
    public void handleAccess() {
        System.out.println("What would you like to do today?");
        System.out.println("(1)Inquire Balance\n(2)Read Transaction file\n(3)Generate Bank Statemet\n(4)Add new User");
    }

    @Override
    public String selectAccountOperation() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectAccountOperation'");
    }

    @Override
    public Account openAccount() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'openAccount'");
    }
    
}
