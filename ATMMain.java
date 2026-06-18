// ATM Simulator Project - Main Class
// Student: [Your Name]
// Date: September 2025

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ATMMain extends JFrame {
    private CardValidator cardValidator;
    private AccountManager accountManager;
    private TransactionHandler transactionHandler;
    private ReceiptPrinter receiptPrinter;
    private String currentAccount = "";
    
    // Define a professional color palette
// Define a professional color palette
private static final Color PRIMARY_COLOR = new Color(52, 73, 94);   // Dark Blue-Grey
private static final Color ACCENT_COLOR = new Color(46, 204, 113);  // Green for success/actions
private static final Color WARNING_COLOR = new Color(231, 76, 60);  // Red for warnings/cancellations
private static final Color BACKGROUND_COLOR = new Color(236, 240, 241); // Light Grey
private static final Color TEXT_COLOR = new Color(0, 0, 0);          // Black text (fixed visibility)
private static final Color BUTTON_COLOR = new Color(149, 165, 166); // Grey-Blue for general buttons
private static final Color BUTTON_TEXT_COLOR = Color.BLACK;


    public ATMMain() {
        cardValidator = new CardValidator();
        accountManager = new AccountManager();
        transactionHandler = new TransactionHandler();
        receiptPrinter = new ReceiptPrinter();
        
        setupUI();
        showWelcomeScreen();
    }
    
    private void setupUI() {
        setTitle("ATM Banking System - Student Project");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);
    }
    
    private void showWelcomeScreen() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());
        
        // Header Panel
        JPanel header = new JPanel();
        header.setBackground(PRIMARY_COLOR);
        header.setPreferredSize(new Dimension(700, 70));
        JLabel title = new JLabel("Welcome to ATM Banking System", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(TEXT_COLOR);
        header.add(title);
        
        // Main Panel
        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        
        JLabel cardLabel = new JLabel("Enter Your 16-Digit Card Number:");
        cardLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        JTextField cardField = new JTextField(16);
        cardField.setFont(new Font("Arial", Font.PLAIN, 14));
        cardField.setPreferredSize(new Dimension(200, 30));
        
        JButton insertCard = new JButton("Insert Card");
        insertCard.setFont(new Font("Arial", Font.BOLD, 14));
        insertCard.setBackground(ACCENT_COLOR); // Use accent color for main action
        insertCard.setForeground(BUTTON_TEXT_COLOR);
        insertCard.setPreferredSize(new Dimension(130, 40));
        
        // Button Action
        insertCard.addActionListener(e -> {
            String cardNum = cardField.getText().trim();
            if (cardValidator.isValidCard(cardNum)) {
                showPINScreen(cardNum);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Card Number! Please check and try again.", 
                                            "Card Error", JOptionPane.ERROR_MESSAGE);
                cardField.setText("");
            }
        });
        
        gbc.gridx = 0; gbc.gridy = 0;
        main.add(cardLabel, gbc);
        gbc.gridy = 1;
        main.add(cardField, gbc);
        gbc.gridy = 2;
        main.add(insertCard, gbc);
        
        // Sample cards info
        JLabel info = new JLabel("<html><center>Sample Cards for Testing:<br>" +
                               "1234567890123456 (PIN: 1234)<br>" +
                               "2345678901234567 (PIN: 2345)</center></html>");
        info.setFont(new Font("Arial", Font.ITALIC, 12));
        info.setForeground(new Color(150, 150, 150)); // A more subtle gray
        gbc.gridy = 3;
        main.add(info, gbc);
        
        add(header, BorderLayout.NORTH);
        add(main, BorderLayout.CENTER);
        
        repaint();
        revalidate();
    }
    
    private void showPINScreen(String cardNumber) {
        getContentPane().removeAll();
        setLayout(new BorderLayout());
        
        JPanel header = new JPanel();
        header.setBackground(PRIMARY_COLOR);
        header.setPreferredSize(new Dimension(700, 70));
        JLabel title = new JLabel("Enter Your 4-Digit PIN", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(TEXT_COLOR);
        header.add(title);
        
        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        
        JLabel pinLabel = new JLabel("PIN:");
        pinLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        
        JPasswordField pinField = new JPasswordField(4);
        pinField.setFont(new Font("Arial", Font.BOLD, 20));
        pinField.setHorizontalAlignment(JTextField.CENTER);
        pinField.setPreferredSize(new Dimension(100, 35));
        
        JButton submitBtn = new JButton("Submit");
        submitBtn.setBackground(ACCENT_COLOR);
        submitBtn.setForeground(BUTTON_TEXT_COLOR);
        submitBtn.setFont(new Font("Arial", Font.BOLD, 14));
        submitBtn.setPreferredSize(new Dimension(100, 35));
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(WARNING_COLOR);
        cancelBtn.setForeground(BUTTON_TEXT_COLOR);
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 14));
        cancelBtn.setPreferredSize(new Dimension(100, 35));
        
        // Button Actions
        submitBtn.addActionListener(e -> {
            String pin = new String(pinField.getPassword());
            if (cardValidator.validatePIN(cardNumber, pin)) {
                currentAccount = cardValidator.getAccountNumber(cardNumber);
                showMainMenu();
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect PIN! Please try again.", 
                                            "Authentication Failed", JOptionPane.ERROR_MESSAGE);
                pinField.setText("");
            }
        });
        
        cancelBtn.addActionListener(e -> showWelcomeScreen());
        
        gbc.gridx = 0; gbc.gridy = 0;
        main.add(pinLabel, gbc);
        gbc.gridy = 1;
        main.add(pinField, gbc);
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnPanel.setBackground(BACKGROUND_COLOR);
        btnPanel.add(submitBtn);
        btnPanel.add(cancelBtn);
        
        gbc.gridy = 2;
        main.add(btnPanel, gbc);
        
        add(header, BorderLayout.NORTH);
        add(main, BorderLayout.CENTER);
        
        repaint();
        revalidate();
    }
    
    private void showMainMenu() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());
        
        JPanel header = new JPanel();
        header.setBackground(PRIMARY_COLOR);
        header.setPreferredSize(new Dimension(700, 70));
        JLabel title = new JLabel("ATM Main Menu - Account: " + currentAccount, JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(TEXT_COLOR);
        header.add(title);
        
        JPanel main = new JPanel(new GridLayout(3, 2, 20, 20));
        main.setBackground(BACKGROUND_COLOR);
        main.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        
        String[] menuItems = {"Balance Inquiry", "Withdraw Cash", "Deposit Cash", 
                              "Transfer Money", "Transaction History", "Exit ATM"};
        
        // Use a consistent, formal color for all buttons except Exit
        Color[] buttonColors = {
            BUTTON_COLOR,
            BUTTON_COLOR,
            BUTTON_COLOR,
            BUTTON_COLOR,
            BUTTON_COLOR,
            WARNING_COLOR
        };
        
        for (int i = 0; i < menuItems.length; i++) {
            JButton btn = new JButton(menuItems[i]);
            btn.setFont(new Font("Arial", Font.BOLD, 14));
            btn.setBackground(buttonColors[i]);
            btn.setForeground(BUTTON_TEXT_COLOR);
            btn.setPreferredSize(new Dimension(150, 60));
            btn.setFocusPainted(false);
            
            String action = menuItems[i];
            btn.addActionListener(e -> handleMenuAction(action));
            main.add(btn);
        }
        
        add(header, BorderLayout.NORTH);
        add(main, BorderLayout.CENTER);
        
        repaint();
        revalidate();
    }
    
    private void handleMenuAction(String action) {
        switch (action) {
            case "Balance Inquiry":
                showBalance();
                break;
            case "Withdraw Cash":
                showWithdraw();
                break;
            case "Deposit Cash":
                showDeposit();
                break;
            case "Transfer Money":
                showTransfer();
                break;
            case "Transaction History":
                showHistory();
                break;
            case "Exit ATM":
                JOptionPane.showMessageDialog(this, "Thank you for using our ATM service!\nHave a great day!");
                currentAccount = "";
                showWelcomeScreen();
                break;
        }
    }
    
    private void showBalance() {
        double balance = accountManager.getBalance(currentAccount);
        String message = "Account Number: " + currentAccount + "\nCurrent Balance: ₹" + String.format("%.2f", balance);
        
        int choice = JOptionPane.showConfirmDialog(this, message + "\n\nWould you like to print a receipt?", 
                                                 "Balance Inquiry", JOptionPane.YES_NO_OPTION, 
                                                 JOptionPane.INFORMATION_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            receiptPrinter.printBalanceReceipt(currentAccount, balance);
            JOptionPane.showMessageDialog(this, "Receipt printed successfully!");
        }
        
        transactionHandler.addTransaction(currentAccount, "Balance Inquiry", 0);
        showMainMenu();
    }
    
    private void showWithdraw() {
        String[] amounts = {"₹20", "₹50", "₹100", "₹200", "₹500", "Other Amount"};
        String choice = (String) JOptionPane.showInputDialog(this, "Select withdrawal amount:", 
                                                             "Cash Withdrawal", JOptionPane.QUESTION_MESSAGE, 
                                                             null, amounts, amounts[0]);
        if (choice != null) {
            double amount = 0;
            if (choice.equals("Other Amount")) {
                String input = JOptionPane.showInputDialog(this, "Enter amount to withdraw:");
                if (input != null && !input.trim().isEmpty()) {
                    try {
                        amount = Double.parseDouble(input);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Invalid amount entered!");
                        showMainMenu();
                        return;
                    }
                } else {
                    showMainMenu();
                    return;
                }
            } else {
                amount = Double.parseDouble(choice.substring(1));
            }
            
            if (amount > 0 && accountManager.withdraw(currentAccount, amount)) {
                transactionHandler.addTransaction(currentAccount, "Withdrawal", amount);
                JOptionPane.showMessageDialog(this, "Withdrawal successful!\nAmount: ₹" + String.format("%.2f", amount));
                
                int receiptChoice = JOptionPane.showConfirmDialog(this, "Print receipt?", 
                                                                 "Receipt", JOptionPane.YES_NO_OPTION);
                if (receiptChoice == JOptionPane.YES_OPTION) {
                    double newBalance = accountManager.getBalance(currentAccount);
                    receiptPrinter.printTransactionReceipt(currentAccount, "Withdrawal", amount, newBalance);
                    JOptionPane.showMessageDialog(this, "Receipt printed!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Transaction failed!\nInsufficient funds or invalid amount.", 
                                            "Transaction Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        showMainMenu();
    }
    
    private void showDeposit() {
        String input = JOptionPane.showInputDialog(this, "Enter deposit amount:");
        if (input != null && !input.trim().isEmpty()) {
            try {
                double amount = Double.parseDouble(input);
                if (amount > 0 && accountManager.deposit(currentAccount, amount)) {
                    transactionHandler.addTransaction(currentAccount, "Deposit", amount);
                    JOptionPane.showMessageDialog(this, "Deposit successful!\nAmount: ₹" + String.format("%.2f", amount));
                    
                    int receiptChoice = JOptionPane.showConfirmDialog(this, "Print receipt?", 
                                                                     "Receipt", JOptionPane.YES_NO_OPTION);
                    if (receiptChoice == JOptionPane.YES_OPTION) {
                        double newBalance = accountManager.getBalance(currentAccount);
                        receiptPrinter.printTransactionReceipt(currentAccount, "Deposit", amount, newBalance);
                        JOptionPane.showMessageDialog(this, "Receipt printed!");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid deposit amount!");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number!");
            }
        }
        showMainMenu();
    }
    
    private void showTransfer() {
        JPanel transferPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JLabel toLabel = new JLabel("To Account:");
        JTextField toField = new JTextField(15);
        JLabel amountLabel = new JLabel("Amount:");
        JTextField amountField = new JTextField(15);
        
        gbc.gridx = 0; gbc.gridy = 0;
        transferPanel.add(toLabel, gbc);
        gbc.gridx = 1;
        transferPanel.add(toField, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        transferPanel.add(amountLabel, gbc);
        gbc.gridx = 1;
        transferPanel.add(amountField, gbc);
        
        int result = JOptionPane.showConfirmDialog(this, transferPanel, 
                                                 "Money Transfer", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            String toAccount = toField.getText().trim();
            String amountStr = amountField.getText().trim();
            
            if (!toAccount.isEmpty() && !amountStr.isEmpty()) {
                try {
                    double amount = Double.parseDouble(amountStr);
                    if (accountManager.transfer(currentAccount, toAccount, amount)) {
                        transactionHandler.addTransaction(currentAccount, "Transfer to " + toAccount, amount);
                        JOptionPane.showMessageDialog(this, "Transfer successful!\nAmount: ₹" + String.format("%.2f", amount) + 
                                                     "\nTo Account: " + toAccount);
                        
                        int receiptChoice = JOptionPane.showConfirmDialog(this, "Print receipt?", 
                                                                         "Receipt", JOptionPane.YES_NO_OPTION);
                        if (receiptChoice == JOptionPane.YES_OPTION) {
                            double newBalance = accountManager.getBalance(currentAccount);
                            receiptPrinter.printTransferReceipt(currentAccount, toAccount, amount, newBalance);
                            JOptionPane.showMessageDialog(this, "Receipt printed!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Transfer failed!\nCheck account number and balance.", 
                                                     "Transfer Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid amount!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
            }
        }
        showMainMenu();
    }
    
    private void showHistory() {
        String history = transactionHandler.getTransactionHistory(currentAccount);
        
        JTextArea textArea = new JTextArea(15, 40);
        textArea.setText(history);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JButton printButton = new JButton("Print History");
        printButton.addActionListener(e -> {
            receiptPrinter.printTransactionHistory(currentAccount, history);
            JOptionPane.showMessageDialog(this, "Transaction history printed!");
        });
        
        panel.add(printButton, BorderLayout.SOUTH);
        
        JOptionPane.showMessageDialog(this, panel, "Transaction History", JOptionPane.INFORMATION_MESSAGE);
        showMainMenu();
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new ATMMain().setVisible(true);
        });
    }
}