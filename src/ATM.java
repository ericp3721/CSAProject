import java.util.Scanner;

public class ATM {
    private Customer customer;
    private Account savingsAccount;
    private Account checkingAccount;
    private TransactionHistory transactionHistory;
    private Scanner scanner;
    private Account selectedAccount;
    private int accountTransactionCounter;
    private int securityTransactionCounter;
    private int count;


    public ATM() {
        this.scanner = new Scanner(System.in);
        this.accountTransactionCounter = 0;
        this.securityTransactionCounter = 0;
    }
    public void start() {
        welcomeScreen();
        createCustomer();
        askForPassword();
        mainMenu();
    }

    private void welcomeScreen() {
        System.out.println("Welcome to the Bank ATM!");
    }

    private void askForPassword() {
        System.out.print("Enter your password: ");
        int enteredPin = scanner.nextInt();

        while (enteredPin != customer.getPassword()) {
            System.out.println("Wrong Password, please try again: ");
            System.out.print("Enter your password: ");
            enteredPin = scanner.nextInt();
        }
    }

    private void displayAccountMenu() {
        System.out.println("Select Account:");
        System.out.println("1. Savings Account");
        System.out.println("2. Checking Account");
    }

    private int getUserChoice() {
        int choice = 0;
        boolean isValidInput = false;

        while (!isValidInput) {
            System.out.print("Enter your choice: ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                isValidInput = true;
            } else {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.next();
            }
        }

        return choice;
    }

    private Account getSelectedAccount() {
        displayAccountMenu();
        int accountChoice = getUserChoice();

        if (accountChoice == 1) {
            return savingsAccount;
        } else if (accountChoice == 2) {
            return checkingAccount;
        } else {
            System.out.println("Invalid account choice. Automatically choosing Savings Account.");
            return savingsAccount;
        }
    }

    public void changePassword() {
        System.out.print("Enter new Password: ");
        int newPassword = scanner.nextInt();
        customer.setPassword(newPassword);
        System.out.println("Password changed successfully!");
        transactionHistory.addSecurity("Password", "Changed", 6);
        displayReceipt("Password Changed", "security");
    }
    //displays a receipt after each action
    private void displayReceipt(String action, double amount, String transactionType) {
        String result = "Successful";
        String transactionID;

        if (transactionType.equals("account")) {
            transactionID = "A" + String.valueOf(10000 + accountTransactionCounter++).substring(1);
            if (transactionType.equals("withdraw")) {
            } else {
                selectedAccount = null;
            }
        } else {
            transactionID = "S" + String.valueOf(10000 + securityTransactionCounter++).substring(1);
        }

        String transactionSummary = action + " $" + amount;

        if (transactionType.equals("transfer")) {
            transactionSummary += " from " + selectedAccount.getUserName();
        }

        System.out.println("Receipt:");
        System.out.println("Transaction ID: " + transactionID);
        System.out.println("Action: " + transactionSummary);
        System.out.println("Result: " + result);

        if (selectedAccount != null) {
            System.out.println("Current Balance: $" + selectedAccount.getBalance());
        }

        System.out.println();
    }

    private void displayReceipt(String action, String transactionType) {
        String result = "Successful";
        String transactionID;

        if (transactionType.equals("account")) {
            transactionID = "A" + String.valueOf(10000 + accountTransactionCounter++).substring(1);
        } else {
            transactionID = "S" + String.valueOf(10000 + securityTransactionCounter++).substring(1);
        }

        System.out.println("Receipt:");
        System.out.println("Transaction ID: " + transactionID);
        System.out.println("Action: " + action);
        System.out.println("Result: " + result);
    }

    private void withdrawMoney() {
        Account selectedAccount = getSelectedAccount();  // Use the returned account directly

        if (selectedAccount != null) {
            System.out.print("Enter the amount to withdraw: $");
            double amount = scanner.nextDouble();

            if (amount % 5 != 0) {
                System.out.println("Invalid amount. Withdrawal amount must be a multiple of $5.");
            } else if (selectedAccount.getBalance() < amount) {
                System.out.println("Not enough funds. Withdrawal failed.");
            } else {
                int twentyBills = (int) (amount / 20);
                int fiveBills = (int) ((amount % 20) / 5);

                selectedAccount.withdraw(amount);

                String action = "Withdraw";
                String result = "Successful";
                transactionHistory.addTransaction(action, result, amount, selectedAccount, 1);

                System.out.println("Withdrawal successful. Dispensing bills:");
                System.out.println("$20 bills: " + twentyBills);
                System.out.println("$5 bills: " + fiveBills);
            }
        } else {
            System.out.println("Invalid account choice. Please try again.");
        }
    }

    private void depositMoney() {
        displayAccountMenu();
        int accountChoice = getUserChoice();

        Account selectedAccount;

        if (accountChoice == 1) {
            selectedAccount = savingsAccount;
        } else if (accountChoice == 2) {
            selectedAccount = checkingAccount;
        } else {
            System.out.println("Invalid account choice. Automatically choosing Savings Account.");
            selectedAccount = savingsAccount;
        }

        System.out.print("Enter the amount to deposit: $");
        double amount = scanner.nextDouble();

        if (amount > 0) {
            selectedAccount.deposit(amount);

            // Update transaction history
            String action = "Deposit";
            String result = "Successful";
            transactionHistory.addTransaction(action, result, amount, selectedAccount, 2);

            System.out.println("Deposit successful. New balance: $" + selectedAccount.getBalance());
            displayReceipt(action, amount, "account");
        } else {
            System.out.println("Invalid deposit amount. Please enter an amount greater than zero.");
        }
    }


    private void transferMoney() {
        displayAccountMenu();
        System.out.println("Select source account:");
        int sourceChoice = getUserChoice();
        Account fromAccount = getAccountFromChoice(sourceChoice);

        displayAccountMenu();
        System.out.println("Select destination account:");
        int destinationChoice = getUserChoice();
        Account toAccount = getAccountFromChoice(destinationChoice);

        if (fromAccount != null && toAccount != null) {
            System.out.print("Enter the amount to transfer: $");
            double amount = scanner.nextDouble();

            if (amount > 0) {
                if (fromAccount.transferTo(toAccount, amount)) {

                    // Update transaction history for both source and destination accounts
                    String action = "Transfer";
                    String result = "Successful";
                    transactionHistory.addTransaction(action, result, amount, fromAccount, 3);
                    transactionHistory.addTransaction(action, result, amount, toAccount, 3);

                    System.out.println("Transfer successful. New balances:");
                    System.out.println(fromAccount.getUserName() + ": $" + fromAccount.getBalance());
                    System.out.println(toAccount.getUserName() + ": $" + toAccount.getBalance());
                    displayReceipt(action, amount, "account");
                } else {
                    System.out.println("Transfer failed. Not enough funds in " + fromAccount.getUserName() + " account.");
                }
            } else {
                System.out.println("Invalid transfer amount. Please enter an amount greater than zero.");
            }
        } else {
            System.out.println("Invalid account choice. Please try again.");
        }
    }

    //gets type of account based on choice
    private Account getAccountFromChoice(int accountChoice) {
        if (accountChoice == 1) {
            return savingsAccount;
        } else if (accountChoice == 2) {
            return checkingAccount;
        } else {
            System.out.println("Invalid account choice. Automatically choosing Savings Account.");
            return savingsAccount;
        }
    }

    public void getBalances() {
        System.out.println("Savings Account Balance: $" + savingsAccount.getBalance());
        System.out.println("Checking Account Balance: $" + checkingAccount.getBalance());
        System.out.println("Balance checked");
        transactionHistory.addSecurity("Balance", "Checked", 4);
        displayReceipt("Balance checked", "security");
    }

    public void getTransactionHistory() {
        System.out.println("Transaction History:");
        System.out.println(transactionHistory.getHistory());
        transactionHistory.addSecurity("History", "Checked", 5);
        displayReceipt("History Checked", "security");
    }

    //user info/creation
    private void createCustomer() {
        System.out.print("Enter your username: ");
        String name = scanner.nextLine();
        System.out.print("Choose a password: ");
        int password = scanner.nextInt();

        customer = new Customer(name, password);
        savingsAccount = new Account("Savings", customer);
        checkingAccount = new Account("Checking", customer);
        transactionHistory = new TransactionHistory();

        System.out.println("Account created!");
    }
    //displays main menu
    private void mainMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;

        while (choice != 7) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Withdraw money");
            System.out.println("2. Deposit money");
            System.out.println("3. Transfer money between accounts");
            System.out.println("4. Get account balances");
            System.out.println("5. Get transaction history");
            System.out.println("6. Change PIN");
            System.out.println("7. Exit");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            if (choice == 1) {
                withdrawMoney();
            } else if (choice == 2) {
                depositMoney();
            } else if (choice == 3) {
                transferMoney();
            } else if (choice == 4) {
                askForPassword(); //  ask for password after getting balances
                getBalances();
            } else if (choice == 5) {
                askForPassword(); //  ask for password after checking transaction history
                getTransactionHistory();
            } else if (choice == 6) {
                askForPassword(); //  ask for password before changing PIN
                changePassword();
            } else {
                System.out.println("Invalid choice, try again: ");
            }

            askForPassword(); // Ask for password after each action
        }

        System.out.println("Thank you for using our bank, goodbye!");
    }
}