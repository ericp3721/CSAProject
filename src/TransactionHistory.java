public class TransactionHistory {
    private String history;
    private int accountTransactionCounter;
    private int securityTransactionCounter;

    public TransactionHistory() {
        this.history = "";
        this.accountTransactionCounter = 0;
        this.securityTransactionCounter = 0;
    }

    public String getHistory() {
        return history;
    }

    public void addTransaction(String action, String result, double amount, Account account, int option) {
        double roundedAmount = Math.round(amount * 100.0) / 100.0;
        double roundedBalance = Math.round(account.getBalance() * 100.0) / 100.0;

        String transactionDetails = generateTransactionID(option) + ": " + action + ": " + result + " $" + roundedAmount
                + " to " + account.getUserName() + " account. New balance: $" + roundedBalance;

        history += transactionDetails + "\n";
    }

    public void addSecurity(String action, String result, int option) {
        String transactionDetails = generateTransactionID(option) + ": " + action + " " + result;
        history += transactionDetails + "\n";
    }

    private String generateTransactionID(int option) {
        int counter;
        String prefix;

        if (option == 1 || option == 2 || option == 3) {
            counter = ++accountTransactionCounter;
            prefix = "A";
        } else {
            counter = ++securityTransactionCounter;
            prefix = "S";
        }

        return prefix + String.valueOf(10000 + counter).substring(1);
    }

}