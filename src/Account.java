public class Account {
    private String userName;
    private Customer owner;
    private double balance;

    public Account(String userName, Customer owner) {
        this.userName = userName;
        this.owner = owner;
        this.balance = 0.0;
    }


    public String getUserName() {
        return userName;
    }

    public double getBalance() {
        return balance;
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }
    public boolean transfer(Account toAccount, double amount) {
        if (amount <= 0 || balance < amount) {
            return false;  // Transfer failed due to invalid amount or insufficient funds
        }

        // Deduct the amount from the source account (this account)
        balance -= amount;

        // Add the amount to the destination account (toAccount)
        toAccount.balance += amount;

        return true;  // Transfer successful
    }
    public boolean transferTo(Account targetAccount, double amount) {
        if (withdraw(amount)) {
            targetAccount.deposit(amount);
        }
        return false;
    }
}