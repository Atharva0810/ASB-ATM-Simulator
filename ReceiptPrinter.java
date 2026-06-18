// ReceiptPrinter.java - Handles all receipt printing functionality
// Student Project: ATM Simulator

import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.*;

public class ReceiptPrinter {
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    private int receiptCounter;
    private final String BANK_NAME = "STUDENT BANK ATM";
    private final String BANK_ADDRESS = "123 University Street, Student City";
    private final String BANK_PHONE = "(555) 123-4567";
    
    // Constructor
    public ReceiptPrinter() {
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        timeFormat = new SimpleDateFormat("HH:mm:ss");
        receiptCounter = 1000; // Start receipt numbers from 1001
        
        System.out.println("Receipt Printer initialized successfully!");
        
        // Create receipts directory if it doesn't exist
        createReceiptsDirectory();
    }
    
    // Create receipts directory
    private void createReceiptsDirectory() {
        File directory = new File("receipts");
        if (!directory.exists()) {
            directory.mkdirs();
            System.out.println("Created receipts directory");
        }
    }
    
    // Generate receipt header
    private String generateReceiptHeader() {
        Date now = new Date();
        String receiptNumber = String.format("R%06d", ++receiptCounter);
        
        StringBuilder header = new StringBuilder();
        header.append("*".repeat(40)).append("\n");
        header.append("           ").append(BANK_NAME).append("\n");
        header.append("       ").append(BANK_ADDRESS).append("\n");
        header.append("           ").append(BANK_PHONE).append("\n");
        header.append("*".repeat(40)).append("\n");
        header.append("Receipt #: ").append(receiptNumber).append("\n");
        header.append("Date: ").append(dateFormat.format(now)).append("\n");
        header.append("Time: ").append(timeFormat.format(now)).append("\n");
        header.append("-".repeat(40)).append("\n");
        
        return header.toString();
    }
    
    // Generate receipt footer
    private String generateReceiptFooter() {
        StringBuilder footer = new StringBuilder();
        footer.append("-".repeat(40)).append("\n");
        footer.append("Thank you for using ").append(BANK_NAME).append("!\n");
        footer.append("Keep this receipt for your records.\n");
        footer.append("For assistance call: ").append(BANK_PHONE).append("\n");
        footer.append("*".repeat(40)).append("\n\n");
        
        return footer.toString();
    }
    
    // Print balance inquiry receipt
    public void printBalanceReceipt(String accountNumber, double balance) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            System.out.println("Cannot print receipt: Invalid account number");
            return;
        }
        
        StringBuilder receipt = new StringBuilder();
        receipt.append(generateReceiptHeader());
        receipt.append("TRANSACTION TYPE: BALANCE INQUIRY\n");
        receipt.append("Account Number: ").append(accountNumber).append("\n");
        receipt.append("Current Balance: ₹").append(String.format("%.2f", balance)).append("\n");
        receipt.append(generateReceiptFooter());
        
        // Print to console
        System.out.println("\n" + "=".repeat(42));
        System.out.println("           PRINTING RECEIPT");
        System.out.println("=".repeat(42));
        System.out.print(receipt.toString());
        
        // Save to file
        saveReceiptToFile("balance_" + accountNumber + "_" + receiptCounter + ".txt", receipt.toString());
    }
    
    // Print transaction receipt (withdrawal, deposit)
    public void printTransactionReceipt(String accountNumber, String transactionType, 
                                       double amount, double newBalance) {
        if (accountNumber == null || transactionType == null) {
            System.out.println("Cannot print receipt: Invalid parameters");
            return;
        }
        
        StringBuilder receipt = new StringBuilder();
        receipt.append(generateReceiptHeader());
        receipt.append("TRANSACTION TYPE: ").append(transactionType.toUpperCase()).append("\n");
        receipt.append("Account Number: ").append(accountNumber).append("\n");
        receipt.append("Transaction Amount: ₹").append(String.format("%.2f", amount)).append("\n");
        receipt.append("New Balance: ₹").append(String.format("%.2f", newBalance)).append("\n");
        
        if (transactionType.toLowerCase().contains("withdrawal")) {
            receipt.append("\nPlease count your cash before leaving.\n");
        } else if (transactionType.toLowerCase().contains("deposit")) {
            receipt.append("\nDeposit has been credited to your account.\n");
        }
        
        receipt.append(generateReceiptFooter());
        
        // Print to console
        System.out.println("\n" + "=".repeat(42));
        System.out.println("           PRINTING RECEIPT");
        System.out.println("=".repeat(42));
        System.out.print(receipt.toString());
        
        // Save to file
        String fileName = transactionType.toLowerCase() + "_" + accountNumber + "_" + receiptCounter + ".txt";
        saveReceiptToFile(fileName, receipt.toString());
    }
    
    // Print transfer receipt
    public void printTransferReceipt(String fromAccount, String toAccount, 
                                    double amount, double newBalance) {
        if (fromAccount == null || toAccount == null) {
            System.out.println("Cannot print receipt: Invalid account numbers");
            return;
        }
        
        StringBuilder receipt = new StringBuilder();
        receipt.append(generateReceiptHeader());
        receipt.append("TRANSACTION TYPE: MONEY TRANSFER\n");
        receipt.append("From Account: ").append(fromAccount).append("\n");
        receipt.append("To Account: ").append(toAccount).append("\n");
        receipt.append("Transfer Amount: ₹").append(String.format("%.2f", amount)).append("\n");
        receipt.append("Remaining Balance: ₹").append(String.format("%.2f", newBalance)).append("\n");
        receipt.append("\nTransfer completed successfully.\n");
        receipt.append("Beneficiary will receive funds immediately.\n");
        receipt.append(generateReceiptFooter());
        
        // Print to console
        System.out.println("\n" + "=".repeat(42));
        System.out.println("           PRINTING RECEIPT");
        System.out.println("=".repeat(42));
        System.out.print(receipt.toString());
        
        // Save to file
        String fileName = "transfer_" + fromAccount + "_to_" + toAccount + "_" + receiptCounter + ".txt";
        saveReceiptToFile(fileName, receipt.toString());
    }
    
    // Print transaction history receipt
    public void printTransactionHistory(String accountNumber, String historyData) {
        if (accountNumber == null || historyData == null) {
            System.out.println("Cannot print receipt: Invalid parameters");
            return;
        }
        
        StringBuilder receipt = new StringBuilder();
        receipt.append(generateReceiptHeader());
        receipt.append("TRANSACTION HISTORY REPORT\n");
        receipt.append("Account Number: ").append(accountNumber).append("\n");
        receipt.append("-".repeat(40)).append("\n");
        receipt.append(historyData);
        receipt.append(generateReceiptFooter());
        
        // Print to console
        System.out.println("\n" + "=".repeat(42));
        System.out.println("         PRINTING HISTORY");
        System.out.println("=".repeat(42));
        System.out.print(receipt.toString());
        
        // Save to file
        String fileName = "history_" + accountNumber + "_" + receiptCounter + ".txt";
        saveReceiptToFile(fileName, receipt.toString());
    }
    
    // Print mini statement
    public void printMiniStatement(String accountNumber, String recentTransactions, double currentBalance) {
        if (accountNumber == null || recentTransactions == null) {
            System.out.println("Cannot print receipt: Invalid parameters");
            return;
        }
        
        StringBuilder receipt = new StringBuilder();
        receipt.append(generateReceiptHeader());
        receipt.append("MINI STATEMENT\n");
        receipt.append("Account Number: ").append(accountNumber).append("\n");
        receipt.append("Current Balance: ₹").append(String.format("%.2f", currentBalance)).append("\n");
        receipt.append("-".repeat(40)).append("\n");
        receipt.append(recentTransactions);
        receipt.append(generateReceiptFooter());
        
        // Print to console
        System.out.println("\n" + "=".repeat(42));
        System.out.println("        PRINTING MINI STATEMENT");
        System.out.println("=".repeat(42));
        System.out.print(receipt.toString());
        
        // Save to file
        String fileName = "mini_statement_" + accountNumber + "_" + receiptCounter + ".txt";
        saveReceiptToFile(fileName, receipt.toString());
    }
    
    // Save receipt to file
    private void saveReceiptToFile(String fileName, String receiptContent) {
        try {
            File receiptFile = new File("receipts", fileName);
            FileWriter writer = new FileWriter(receiptFile);
            writer.write(receiptContent);
            writer.close();
            System.out.println("Receipt saved to file: " + receiptFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error saving receipt to file: " + e.getMessage());
        }
    }
    
    // Print error receipt
    public void printErrorReceipt(String accountNumber, String errorMessage) {
        StringBuilder receipt = new StringBuilder();
        receipt.append(generateReceiptHeader());
        receipt.append("TRANSACTION ERROR\n");
        if (accountNumber != null && !accountNumber.trim().isEmpty()) {
            receipt.append("Account Number: ").append(accountNumber).append("\n");
        }
        receipt.append("Error: ").append(errorMessage).append("\n");
        receipt.append("Please contact customer service for assistance.\n");
        receipt.append(generateReceiptFooter());
        
        // Print to console
        System.out.println("\n" + "=".repeat(42));
        System.out.println("         PRINTING ERROR RECEIPT");
        System.out.println("=".repeat(42));
        System.out.print(receipt.toString());
        
        // Save to file
        String fileName = "error_" + System.currentTimeMillis() + ".txt";
        saveReceiptToFile(fileName, receipt.toString());
    }
    
    // Print welcome receipt (when card is inserted)
    public void printWelcomeReceipt(String accountNumber, String customerName) {
        StringBuilder receipt = new StringBuilder();
        receipt.append(generateReceiptHeader());
        receipt.append("WELCOME TO ATM SERVICE\n");
        receipt.append("Account Number: ").append(accountNumber).append("\n");
        if (customerName != null && !customerName.trim().isEmpty()) {
            receipt.append("Customer: ").append(customerName).append("\n");
        }
        receipt.append("Session started successfully.\n");
        receipt.append("Please complete your transactions.\n");
        receipt.append(generateReceiptFooter());
        
        // Print to console
        System.out.println("\n" + "=".repeat(42));
        System.out.println("        PRINTING WELCOME RECEIPT");
        System.out.println("=".repeat(42));
        System.out.print(receipt.toString());
        
        // Save to file
        String fileName = "welcome_" + accountNumber + "_" + receiptCounter + ".txt";
        saveReceiptToFile(fileName, receipt.toString());
    }
    
    // Print session end receipt
    public void printSessionEndReceipt(String accountNumber, int transactionsPerformed) {
        StringBuilder receipt = new StringBuilder();
        receipt.append(generateReceiptHeader());
        receipt.append("SESSION SUMMARY\n");
        receipt.append("Account Number: ").append(accountNumber).append("\n");
        receipt.append("Transactions Performed: ").append(transactionsPerformed).append("\n");
        receipt.append("Session ended successfully.\n");
        receipt.append("Please take your card.\n");
        receipt.append("Thank you for banking with us!\n");
        receipt.append(generateReceiptFooter());
        
        // Print to console
        System.out.println("\n" + "=".repeat(42));
        System.out.println("       PRINTING SESSION END RECEIPT");
        System.out.println("=".repeat(42));
        System.out.print(receipt.toString());
        
        // Save to file
        String fileName = "session_end_" + accountNumber + "_" + receiptCounter + ".txt";
        saveReceiptToFile(fileName, receipt.toString());
    }
    
    // Get total receipts printed
    public int getTotalReceiptsPrinted() {
        return receiptCounter - 1000; // Subtract initial counter value
    }
    
    // Get receipt counter for next receipt
    public int getNextReceiptNumber() {
        return receiptCounter + 1;
    }
    
    // Print daily summary (admin function)
    public void printDailySummary(String date, int totalTransactions, double totalAmount) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("*".repeat(50)).append("\n");
        receipt.append("              DAILY SUMMARY REPORT\n");
        receipt.append("                 ").append(BANK_NAME).append("\n");
        receipt.append("*".repeat(50)).append("\n");
        receipt.append("Date: ").append(date).append("\n");
        receipt.append("Report Generated: ").append(timeFormat.format(new Date())).append("\n");
        receipt.append("-".repeat(50)).append("\n");
        receipt.append("Total Transactions: ").append(totalTransactions).append("\n");
        receipt.append("Total Amount Processed: ₹").append(String.format("%.2f", totalAmount)).append("\n");
        receipt.append("Total Receipts Printed: ").append(getTotalReceiptsPrinted()).append("\n");
        receipt.append("-".repeat(50)).append("\n");
        receipt.append("System Status: Online\n");
        receipt.append("Last Receipt #: R").append(String.format("%06d", receiptCounter)).append("\n");
        receipt.append("*".repeat(50)).append("\n\n");
        
        // Print to console
        System.out.println("\n" + "=".repeat(52));
        System.out.println("            PRINTING DAILY SUMMARY");
        System.out.println("=".repeat(52));
        System.out.print(receipt.toString());
        
        // Save to file
        String fileName = "daily_summary_" + date.replace("/", "_") + ".txt";
        saveReceiptToFile(fileName, receipt.toString());
    }
    
    // Test printer functionality
    public void testPrinter() {
        System.out.println("\n" + "=".repeat(42));
        System.out.println("           PRINTER TEST");
        System.out.println("=".repeat(42));
        System.out.println("Printer Status: Online");
        System.out.println("Paper Status: OK");
        System.out.println("Ink Status: OK");
        System.out.println("Last Receipt: R" + String.format("%06d", receiptCounter));
        System.out.println("Total Receipts Printed: " + getTotalReceiptsPrinted());
        System.out.println("Receipts Directory: receipts/");
        System.out.println("=".repeat(42) + "\n");
    }
    
    // Clean old receipt files (keep only recent ones)
    public void cleanOldReceipts(int daysToKeep) {
        File receiptsDir = new File("receipts");
        if (!receiptsDir.exists()) {
            return;
        }
        
        long cutoffTime = System.currentTimeMillis() - (daysToKeep * 24 * 60 * 60 * 1000L);
        File[] files = receiptsDir.listFiles();
        
        if (files != null) {
            int deletedCount = 0;
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt") && 
                    file.lastModified() < cutoffTime) {
                    if (file.delete()) {
                        deletedCount++;
                    }
                }
            }
            System.out.println("Cleaned " + deletedCount + " old receipt files (older than " + 
                             daysToKeep + " days)");
        }
    }
    
    // Get receipts directory info
    public void printReceiptDirectoryInfo() {
        File receiptsDir = new File("receipts");
        if (!receiptsDir.exists()) {
            System.out.println("Receipts directory does not exist");
            return;
        }
        
        File[] files = receiptsDir.listFiles();
        long totalSize = 0;
        int fileCount = 0;
        
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    totalSize += file.length();
                    fileCount++;
                }
            }
        }
        
        System.out.println("\n=== RECEIPTS DIRECTORY INFO ===");
        System.out.println("Location: " + receiptsDir.getAbsolutePath());
        System.out.println("Total receipt files: " + fileCount);
        System.out.println("Total size: " + (totalSize / 1024) + " KB");
        System.out.println("Average file size: " + 
                          (fileCount > 0 ? (totalSize / fileCount) + " bytes" : "N/A"));
        System.out.println("==============================\n");
    }
}