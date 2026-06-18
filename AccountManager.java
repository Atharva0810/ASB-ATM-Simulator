// AccountManager.java - Manages account balances and transactions
// Student Project: ATM Simulator

import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AccountManager {
    private Map<String, Account> accounts;
    private SimpleDateFormat dateFormat;
    
    // Inner class to store account information
    private class Account {
        String accountNumber;
        String accountHolder;
        double balance;
        Date createdDate;
        boolean isActive;
        
        public Account(String accountNumber, String accountHolder, double initialBalance) {
            this.accountNumber = accountNumber;
            this.accountHolder = accountHolder;
            this.balance = initialBalance;
            this.createdDate = new Date();
            this.isActive = true;
        }
        
        @Override
        public String toString() {
            return String.format("Account: %s | Holder: %s | Balance: ₹%.2f | Active: %s", 
                               accountNumber, accountHolder, balance, isActive);
        }
    }
    
    // Constructor
    public AccountManager() {
        accounts = new HashMap<>();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        initializeSampleAccounts();
    }
    
    // Initialize sample accounts for testing
    private void initializeSampleAccounts() {
        createAccount("ACC001", "John Smith", 1500.00);
        createAccount("ACC002", "Jane Johnson", 2750.50);
        createAccount("ACC003", "Mike Brown", 500.25);
        createAccount("ACC004", "Sarah Davis", 3200.75);
        createAccount("ACC005", "Tom Wilson", 825.00);
        
        System.out.println("Sample accounts initialized successfully!");
        System.out.println("Total accounts created: " + accounts.size());
    }
    
    // Create new account
    public boolean createAccount(String accountNumber, String accountHolder, double initialBalance) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            System.out.println("Account number cannot be empty");
            return false;
        }
        
        if (accountHolder == null || accountHolder.trim().isEmpty()) {
            System.out.println("Account holder name cannot be empty");
            return false;
        }
        
        if (initialBalance < 0) {
            System.out.println("Initial balance cannot be negative");
            return false;
        }
        
        if (accounts.containsKey(accountNumber)) {
            System.out.println("Account already exists: " + accountNumber);
            return false;
        }
        
        Account newAccount = new Account(accountNumber, accountHolder, initialBalance);
        accounts.put(accountNumber, newAccount);
        
        System.out.println("Account created successfully: " + accountNumber + 
                          " for " + accountHolder + " with balance ₹" + initialBalance);
        return true;
    }
    
    // Get account balance
    public double getBalance(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            System.out.println("Account number is empty");
            return -1;
        }
        
        Account account = accounts.get(accountNumber.trim());
        if (account == null) {
            System.out.println("Account not found: " + accountNumber);
            return -1;
        }
        
        if (!account.isActive) {
            System.out.println("Account is inactive: " + accountNumber);
            return -1;
        }
        
        System.out.println("Balance retrieved for " + accountNumber + ": ₹" + account.balance);
        return account.balance;
    }
    
    // Deposit money to account
    public boolean deposit(String accountNumber, double amount) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            System.out.println("Account number is empty");
            return false;
        }
        
        if (amount <= 0) {
            System.out.println("Deposit amount must be positive");
            return false;
        }
        
        Account account = accounts.get(accountNumber.trim());
        if (account == null) {
            System.out.println("Account not found for deposit: " + accountNumber);
            return false;
        }
        
        if (!account.isActive) {
            System.out.println("Cannot deposit to inactive account: " + accountNumber);
            return false;
        }
        
        account.balance += amount;
        System.out.println("Deposit successful: ₹" + amount + " to account " + accountNumber + 
                          ". New balance: ₹" + account.balance);
        return true;
    }
    
    // Withdraw money from account
    public boolean withdraw(String accountNumber, double amount) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            System.out.println("Account number is empty");
            return false;
        }
        
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive");
            return false;
        }
        
        Account account = accounts.get(accountNumber.trim());
        if (account == null) {
            System.out.println("Account not found for withdrawal: " + accountNumber);
            return false;
        }
        
        if (!account.isActive) {
            System.out.println("Cannot withdraw from inactive account: " + accountNumber);
            return false;
        }
        
        if (account.balance < amount) {
            System.out.println("Insufficient funds. Balance: ₹" + account.balance + 
                              ", Requested: ₹" + amount);
            return false;
        }
        
        account.balance -= amount;
        System.out.println("Withdrawal successful: ₹" + amount + " from account " + accountNumber + 
                          ". New balance: ₹" + account.balance);
        return true;
    }
    
    // Transfer money between accounts
    public boolean transfer(String fromAccount, String toAccount, double amount) {
        if (fromAccount == null || toAccount == null || 
            fromAccount.trim().isEmpty() || toAccount.trim().isEmpty()) {
            System.out.println("Account numbers cannot be empty");
            return false;
        }
        
        if (amount <= 0) {
            System.out.println("Transfer amount must be positive");
            return false;
        }
        
        if (fromAccount.equals(toAccount)) {
            System.out.println("Cannot transfer to same account");
            return false;
        }
        
        Account sourceAccount = accounts.get(fromAccount.trim());
        Account targetAccount = accounts.get(toAccount.trim());
        
        if (sourceAccount == null) {
            System.out.println("Source account not found: " + fromAccount);
            return false;
        }
        
        if (targetAccount == null) {
            System.out.println("Target account not found: " + toAccount);
            return false;
        }
        
        if (!sourceAccount.isActive || !targetAccount.isActive) {
            System.out.println("One or both accounts are inactive");
            return false;
        }
        
        if (sourceAccount.balance < amount) {
            System.out.println("Insufficient funds for transfer. Balance: ₹" + sourceAccount.balance + 
                              ", Requested: ₹" + amount);
            return false;
        }
        
        // Perform transfer
        sourceAccount.balance -= amount;
        targetAccount.balance += amount;
        
        System.out.println("Transfer successful: ₹" + amount + " from " + fromAccount + 
                          " to " + toAccount);
        System.out.println("Source account new balance: ₹" + sourceAccount.balance);
        System.out.println("Target account new balance: ₹" + targetAccount.balance);
        return true;
    }
    
    // Check if account exists
    public boolean accountExists(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return false;
        }
        
        return accounts.containsKey(accountNumber.trim());
    }
    
    // Check if account is active
    public boolean isAccountActive(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return false;
        }
        
        Account account = accounts.get(accountNumber.trim());
        return account != null && account.isActive;
    }
    
    // Get account holder name
    public String getAccountHolder(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return null;
        }
        
        Account account = accounts.get(accountNumber.trim());
        if (account != null) {
            return account.accountHolder;
        }
        
        return null;
    }
    
    // Deactivate account
    public boolean deactivateAccount(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return false;
        }
        
        Account account = accounts.get(accountNumber.trim());
        if (account != null) {
            account.isActive = false;
            System.out.println("Account deactivated: " + accountNumber);
            return true;
        }
        
        return false;
    }
    
    // Activate account
    public boolean activateAccount(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return false;
        }
        
        Account account = accounts.get(accountNumber.trim());
        if (account != null) {
            account.isActive = true;
            System.out.println("Account activated: " + accountNumber);
            return true;
        }
        
        return false;
    }
    
    // Get total number of accounts
    public int getTotalAccounts() {
        return accounts.size();
    }
    
    // Get number of active accounts
    public int getActiveAccounts() {
        int activeCount = 0;
        for (Account account : accounts.values()) {
            if (account.isActive) {
                activeCount++;
            }
        }
        return activeCount;
    }
    
    // Get total balance across all accounts
    public double getTotalBalance() {
        double totalBalance = 0;
        for (Account account : accounts.values()) {
            if (account.isActive) {
                totalBalance += account.balance;
            }
        }
        return totalBalance;
    }
    
    // Print account details (for admin purposes)
    public void printAccountDetails(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            System.out.println("Account number is empty");
            return;
        }
        
        Account account = accounts.get(accountNumber.trim());
        if (account == null) {
            System.out.println("Account not found: " + accountNumber);
            return;
        }
        
        System.out.println("\n=== ACCOUNT DETAILS ===");
        System.out.println("Account Number: " + account.accountNumber);
        System.out.println("Account Holder: " + account.accountHolder);
        System.out.println("Balance: ₹" + String.format("%.2f", account.balance));
        System.out.println("Created: " + dateFormat.format(account.createdDate));
        System.out.println("Status: " + (account.isActive ? "Active" : "Inactive"));
        System.out.println("=====================\n");
    }
    
    // Print all accounts summary (for admin purposes)
    public void printAllAccounts() {
        System.out.println("\n=== ALL ACCOUNTS SUMMARY ===");
        System.out.println("Total Accounts: " + accounts.size());
        System.out.println("Active Accounts: " + getActiveAccounts());
        System.out.println("Total Balance: ₹" + String.format("%.2f", getTotalBalance()));
        System.out.println("\nAccount List:");
        
        for (Account account : accounts.values()) {
            System.out.println(account.toString());
        }
        System.out.println("===========================\n");
    }
}