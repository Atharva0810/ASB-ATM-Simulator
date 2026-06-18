// CardValidator.java - Handles card validation and PIN verification
// Student Project: ATM Simulator

import java.util.HashMap;
import java.util.Map;

public class CardValidator {
    private Map<String, CardInfo> cardDatabase;
    
    // Inner class to store card information
    private class CardInfo {
        String cardNumber;
        String pin;
        String accountNumber;
        boolean isActive;
        
        public CardInfo(String cardNumber, String pin, String accountNumber) {
            this.cardNumber = cardNumber;
            this.pin = pin;
            this.accountNumber = accountNumber;
            this.isActive = true;
        }
    }
    
    // Constructor - initialize with sample data
    public CardValidator() {
        cardDatabase = new HashMap<>();
        loadSampleCards();
    }
    
    // Load sample cards for testing
    private void loadSampleCards() {
        // Adding sample cards for demonstration
        addCard("1234567890123456", "1234", "ACC001");
        addCard("2345678901234567", "2345", "ACC002");
        addCard("3456789012345678", "3456", "ACC003");
        addCard("4567890123456789", "4567", "ACC004");
        addCard("5678901234567890", "5678", "ACC005");
        
        System.out.println("Sample cards loaded successfully!");
        System.out.println("Total cards in database: " + cardDatabase.size());
    }
    
    // Add new card to database
    public boolean addCard(String cardNumber, String pin, String accountNumber) {
        if (cardNumber == null || cardNumber.length() != 16 || !cardNumber.matches("\\d+")) {
            System.out.println("Invalid card number format");
            return false;
        }
        
        if (pin == null || pin.length() != 4 || !pin.matches("\\d+")) {
            System.out.println("Invalid PIN format");
            return false;
        }
        
        if (cardDatabase.containsKey(cardNumber)) {
            System.out.println("Card already exists");
            return false;
        }
        
        CardInfo newCard = new CardInfo(cardNumber, pin, accountNumber);
        cardDatabase.put(cardNumber, newCard);
        System.out.println("Card added: " + cardNumber + " -> " + accountNumber);
        return true;
    }
    
    // Validate card number format and existence
    public boolean isValidCard(String cardNumber) {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            System.out.println("Card number is empty");
            return false;
        }
        
        cardNumber = cardNumber.trim();
        
        // Check length
        if (cardNumber.length() != 16) {
            System.out.println("Card number must be 16 digits");
            return false;
        }
        
        // Check if all digits
        if (!cardNumber.matches("\\d+")) {
            System.out.println("Card number must contain only digits");
            return false;
        }
        
        // Check if card exists in database
        if (!cardDatabase.containsKey(cardNumber)) {
            System.out.println("Card not found in database: " + cardNumber);
            return false;
        }
        
        // Check if card is active
        CardInfo card = cardDatabase.get(cardNumber);
        if (!card.isActive) {
            System.out.println("Card is inactive: " + cardNumber);
            return false;
        }
        
        System.out.println("Card validation successful: " + cardNumber);
        return true;
    }
    
    // Validate PIN for given card
    public boolean validatePIN(String cardNumber, String pin) {
        if (cardNumber == null || pin == null) {
            System.out.println("Card number or PIN is null");
            return false;
        }
        
        cardNumber = cardNumber.trim();
        pin = pin.trim();
        
        // Check PIN format
        if (pin.length() != 4 || !pin.matches("\\d+")) {
            System.out.println("Invalid PIN format");
            return false;
        }
        
        // Get card from database
        CardInfo card = cardDatabase.get(cardNumber);
        if (card == null) {
            System.out.println("Card not found for PIN validation");
            return false;
        }
        
        // Verify PIN
        if (card.pin.equals(pin)) {
            System.out.println("PIN validation successful for card: " + cardNumber);
            return true;
        } else {
            System.out.println("PIN validation failed for card: " + cardNumber);
            return false;
        }
    }
    
    // Get account number associated with card
    public String getAccountNumber(String cardNumber) {
        if (cardNumber == null) {
            return null;
        }
        
        CardInfo card = cardDatabase.get(cardNumber.trim());
        if (card != null) {
            System.out.println("Account number retrieved: " + card.accountNumber + " for card: " + cardNumber);
            return card.accountNumber;
        }
        
        System.out.println("No account found for card: " + cardNumber);
        return null;
    }
    
    // Deactivate card (for security purposes)
    public boolean deactivateCard(String cardNumber) {
        if (cardNumber == null) {
            return false;
        }
        
        CardInfo card = cardDatabase.get(cardNumber.trim());
        if (card != null) {
            card.isActive = false;
            System.out.println("Card deactivated: " + cardNumber);
            return true;
        }
        
        System.out.println("Card not found for deactivation: " + cardNumber);
        return false;
    }
    
    // Activate card
    public boolean activateCard(String cardNumber) {
        if (cardNumber == null) {
            return false;
        }
        
        CardInfo card = cardDatabase.get(cardNumber.trim());
        if (card != null) {
            card.isActive = true;
            System.out.println("Card activated: " + cardNumber);
            return true;
        }
        
        System.out.println("Card not found for activation: " + cardNumber);
        return false;
    }
    
    // Change PIN for card
    public boolean changePIN(String cardNumber, String oldPin, String newPin) {
        if (cardNumber == null || oldPin == null || newPin == null) {
            System.out.println("One or more parameters are null");
            return false;
        }
        
        cardNumber = cardNumber.trim();
        oldPin = oldPin.trim();
        newPin = newPin.trim();
        
        // Validate new PIN format
        if (newPin.length() != 4 || !newPin.matches("\\d+")) {
            System.out.println("New PIN format is invalid");
            return false;
        }
        
        CardInfo card = cardDatabase.get(cardNumber);
        if (card != null && card.pin.equals(oldPin)) {
            card.pin = newPin;
            System.out.println("PIN changed successfully for card: " + cardNumber);
            return true;
        }
        
        System.out.println("PIN change failed for card: " + cardNumber);
        return false;
    }
    
    // Check if card is active
    public boolean isCardActive(String cardNumber) {
        if (cardNumber == null) {
            return false;
        }
        
        CardInfo card = cardDatabase.get(cardNumber.trim());
        if (card != null) {
            return card.isActive;
        }
        
        return false;
    }
    
    // Get total number of cards in database
    public int getTotalCards() {
        return cardDatabase.size();
    }
    
    // Get number of active cards
    public int getActiveCards() {
        int activeCount = 0;
        for (CardInfo card : cardDatabase.values()) {
            if (card.isActive) {
                activeCount++;
            }
        }
        return activeCount;
    }
    
    // Print all cards info (for admin purposes)
    public void printAllCards() {
        System.out.println("\n=== CARD DATABASE ===");
        System.out.println("Total Cards: " + cardDatabase.size());
        System.out.println("Active Cards: " + getActiveCards());
        System.out.println("\nCard Details:");
        
        for (CardInfo card : cardDatabase.values()) {
            System.out.printf("Card: %s | Account: %s | PIN: %s | Active: %s%n", 
                            card.cardNumber, card.accountNumber, card.pin, card.isActive);
        }
        System.out.println("===================\n");
    }
}