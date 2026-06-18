import java.util.*;
import java.text.SimpleDateFormat;

public class TransactionHandler {
    private Map<String, ArrayList<Transaction>> transactionHistory;
    private SimpleDateFormat dateFormat;
    private int transactionIdCounter;
    
    private class Transaction {
        int transactionId;
        String accountNumber;
        String transactionType;
        double amount;
        Date timestamp;
        String description;
        
        public Transaction(String accountNumber, String transactionType, double amount) {
            this.transactionId = ++transactionIdCounter;
            this.accountNumber = accountNumber;
            this.transactionType = transactionType;
            this.amount = amount;
            this.timestamp = new Date();
            this.description = generateDescription();
        }
        
        private String generateDescription() {
            switch (transactionType.toLowerCase()) {
                case "withdrawal":
                    return "ATM Withdrawal - ₹" + String.format("%.2f", amount);
                case "deposit":
                    return "ATM Deposit - ₹" + String.format("%.2f", amount);
                case "balance inquiry":
                    return "Balance Inquiry";
                default:
                    if (transactionType.contains("Transfer")) {
                        return transactionType + " - ₹" + String.format("%.2f", amount);
                    }
                    return transactionType + " - ₹" + String.format("%.2f", amount);
            }
        }
        
        @Override
        public String toString() {
            return String.format("[%d] %s | %s | %s", 
                               transactionId,
                               dateFormat.format(timestamp),
                               transactionType,
                               description);
        }
    }
    
    public TransactionHandler() {
        transactionHistory = new HashMap<>();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        transactionIdCounter = 1000;
        
        System.out.println("Transaction Handler initialized successfully!");
    }
    
    public boolean addTransaction(String accountNumber, String transactionType, double amount) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            System.out.println("Account number cannot be empty");
            return false;
        }
        
        if (transactionType == null || transactionType.trim().isEmpty()) {
            System.out.println("Transaction type cannot be empty");
            return false;
        }
        
        accountNumber = accountNumber.trim();
        transactionType = transactionType.trim();
        
        Transaction newTransaction = new Transaction(accountNumber, transactionType, amount);
        
        ArrayList<Transaction> accountTransactions = transactionHistory.get(accountNumber);
        if (accountTransactions == null) {
            accountTransactions = new ArrayList<>();
            transactionHistory.put(accountNumber, accountTransactions);
        }
        
        accountTransactions.add(newTransaction);
        
        System.out.println("Transaction recorded: " + newTransaction.toString());
        return true;
    }
    
    public String getTransactionHistory(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return "Invalid account number";
        }
        
        accountNumber = accountNumber.trim();
        ArrayList<Transaction> accountTransactions = transactionHistory.get(accountNumber);
        
        StringBuilder history = new StringBuilder();
        history.append("TRANSACTION HISTORY\n");
        history.append("Account: ").append(accountNumber).append("\n");
        history.append("Generated: ").append(dateFormat.format(new Date())).append("\n");
        history.append("=" .repeat(50)).append("\n");
        
        if (accountTransactions == null || accountTransactions.isEmpty()) {
            history.append("No transactions found for this account.\n");
        } else {
            history.append("Total Transactions: ").append(accountTransactions.size()).append("\n\n");
            
            accountTransactions.sort((t1, t2) -> t2.timestamp.compareTo(t1.timestamp));
            
            for (Transaction transaction : accountTransactions) {
                history.append(transaction.toString()).append("\n");
            }
        }
        
        history.append("=" .repeat(50)).append("\n");
        
        System.out.println("Transaction history retrieved for account: " + accountNumber);
        return history.toString();
    }
    
    public String getRecentTransactions(String accountNumber, int count) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return "Invalid account number";
        }
        
        if (count <= 0) {
            count = 5; 
        }
        
        accountNumber = accountNumber.trim();
        ArrayList<Transaction> accountTransactions = transactionHistory.get(accountNumber);
        
        StringBuilder history = new StringBuilder();
        history.append("RECENT TRANSACTIONS (Last ").append(count).append(")\n");
        history.append("Account: ").append(accountNumber).append("\n");
        history.append("Generated: ").append(dateFormat.format(new Date())).append("\n");
        history.append("-" .repeat(40)).append("\n");
        
        if (accountTransactions == null || accountTransactions.isEmpty()) {
            history.append("No transactions found.\n");
        } else {
            accountTransactions.sort((t1, t2) -> t2.timestamp.compareTo(t1.timestamp));
            
            int transactionsToShow = Math.min(count, accountTransactions.size());
            
            for (int i = 0; i < transactionsToShow; i++) {
                history.append(accountTransactions.get(i).toString()).append("\n");
            }
        }
        
        history.append("-" .repeat(40)).append("\n");
        return history.toString();
    }
    
    public int getTransactionCount(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return 0;
        }
        
        ArrayList<Transaction> accountTransactions = transactionHistory.get(accountNumber);
        if (accountTransactions == null) {
            return 0;
        }
        
        return accountTransactions.size();
    }
    
    public ArrayList<Transaction> getTransactionsByType(String accountNumber, String transactionType) {
        ArrayList<Transaction> filteredTransactions = new ArrayList<>();
        
        if (accountNumber == null || transactionType == null) {
            return filteredTransactions;
        }
        
        accountNumber = accountNumber.trim();
        transactionType = transactionType.trim().toLowerCase();
        
        ArrayList<Transaction> accountTransactions = transactionHistory.get(accountNumber);
        if (accountTransactions == null) {
            return filteredTransactions;
        }
        
        for (Transaction transaction : accountTransactions) {
            if (transaction.transactionType.toLowerCase().contains(transactionType)) {
                filteredTransactions.add(transaction);
            }
        }
        
        return filteredTransactions;
    }
    
    public double getTotalAmountByType(String accountNumber, String transactionType) {
        double total = 0.0;
        
        ArrayList<Transaction> transactions = getTransactionsByType(accountNumber, transactionType);
        for (Transaction transaction : transactions) {
            total += transaction.amount;
        }
        
        return total;
    }
    
    public String getTransactionsByDateRange(String accountNumber, Date startDate, Date endDate) {
        if (accountNumber == null || startDate == null || endDate == null) {
            return "Invalid parameters";
        }
        
        accountNumber = accountNumber.trim();
        ArrayList<Transaction> accountTransactions = transactionHistory.get(accountNumber);
        
        StringBuilder history = new StringBuilder();
        history.append("TRANSACTIONS BY DATE RANGE\n");
        history.append("Account: ").append(accountNumber).append("\n");
        history.append("From: ").append(dateFormat.format(startDate)).append("\n");
        history.append("To: ").append(dateFormat.format(endDate)).append("\n");
        history.append("-" .repeat(40)).append("\n");
        
        if (accountTransactions == null || accountTransactions.isEmpty()) {
            history.append("No transactions found.\n");
        } else {
            ArrayList<Transaction> filteredTransactions = new ArrayList<>();
            
            for (Transaction transaction : accountTransactions) {
                if (transaction.timestamp.compareTo(startDate) >= 0 && 
                    transaction.timestamp.compareTo(endDate) <= 0) {
                    filteredTransactions.add(transaction);
                }
            }
            
            if (filteredTransactions.isEmpty()) {
                history.append("No transactions found in the specified date range.\n");
            } else {
                filteredTransactions.sort((t1, t2) -> t2.timestamp.compareTo(t1.timestamp));
                
                history.append("Found ").append(filteredTransactions.size()).append(" transactions:\n\n");
                for (Transaction transaction : filteredTransactions) {
                    history.append(transaction.toString()).append("\n");
                }
            }
        }
        
        history.append("-" .repeat(40)).append("\n");
        return history.toString();
    }
    
    public boolean clearTransactionHistory(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return false;
        }
        
        accountNumber = accountNumber.trim();
        ArrayList<Transaction> accountTransactions = transactionHistory.get(accountNumber);
        
        if (accountTransactions != null) {
            int clearedCount = accountTransactions.size();
            accountTransactions.clear();
            System.out.println("Cleared " + clearedCount + " transactions for account: " + accountNumber);
            return true;
        }
        
        return false;
    }
    
    public String getTransactionSummary(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return "Invalid account number";
        }
        
        accountNumber = accountNumber.trim();
        ArrayList<Transaction> accountTransactions = transactionHistory.get(accountNumber);
        
        StringBuilder summary = new StringBuilder();
        summary.append("TRANSACTION SUMMARY\n");
        summary.append("Account: ").append(accountNumber).append("\n");
        summary.append("Generated: ").append(dateFormat.format(new Date())).append("\n");
        summary.append("=" .repeat(30)).append("\n");
        
        if (accountTransactions == null || accountTransactions.isEmpty()) {
            summary.append("No transactions found.\n");
        } else {
            int totalTransactions = accountTransactions.size();
            double totalWithdrawals = getTotalAmountByType(accountNumber, "withdrawal");
            double totalDeposits = getTotalAmountByType(accountNumber, "deposit");
            int withdrawalCount = getTransactionsByType(accountNumber, "withdrawal").size();
            int depositCount = getTransactionsByType(accountNumber, "deposit").size();
            int transferCount = getTransactionsByType(accountNumber, "transfer").size();
            int inquiryCount = getTransactionsByType(accountNumber, "inquiry").size();
            
            summary.append("Total Transactions: ").append(totalTransactions).append("\n");
            summary.append("Withdrawals: ").append(withdrawalCount).append(" (₹").append(String.format("%.2f", totalWithdrawals)).append(")\n");
            summary.append("Deposits: ").append(depositCount).append(" (₹").append(String.format("%.2f", totalDeposits)).append(")\n");
            summary.append("Transfers: ").append(transferCount).append("\n");
            summary.append("Balance Inquiries: ").append(inquiryCount).append("\n");
            
            if (!accountTransactions.isEmpty()) {
                accountTransactions.sort((t1, t2) -> t2.timestamp.compareTo(t1.timestamp));
                Transaction lastTransaction = accountTransactions.get(0);
                summary.append("Last Transaction: ").append(dateFormat.format(lastTransaction.timestamp)).append("\n");
            }
        }
        
        summary.append("=" .repeat(30)).append("\n");
        return summary.toString();
    }
    
    public Set<String> getAccountsWithTransactions() {
        return transactionHistory.keySet();
    }
    
    public void printSystemStatistics() {
        System.out.println("\n=== SYSTEM TRANSACTION STATISTICS ===");
        System.out.println("Accounts with transactions: " + transactionHistory.size());
        
        int totalTransactions = 0;
        for (ArrayList<Transaction> transactions : transactionHistory.values()) {
            totalTransactions += transactions.size();
        }
        
        System.out.println("Total system transactions: " + totalTransactions);
        System.out.println("Next transaction ID: " + (transactionIdCounter + 1));
        System.out.println("=====================================\n");
    }
}