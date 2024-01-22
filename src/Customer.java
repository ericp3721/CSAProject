public class Customer {
    private String userName;
    private int password;
    private Account savingsAccount;
    private Account checkingAccount;
    private TransactionHistory transactionHistory;

    public Customer(String userName, int password) {
        this.userName = userName;
        this.password = password;
        this.transactionHistory = new TransactionHistory();
    }

    public void initializeAccounts() {
        savingsAccount = new Account("Savings", this);
        checkingAccount = new Account("Checking", this);
    }

    public String getUserName() {
        return userName;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public Account getSavingsAccount() {
        return savingsAccount;
    }

    public Account getCheckingAccount() {
        return checkingAccount;
    }

    public TransactionHistory getTransactionHistory() {
        return transactionHistory;
    }
}