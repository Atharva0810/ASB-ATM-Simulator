import java.util.*;
import java.text.SimpleDateFormat;
import java.io.*;

public class CustomerData {
    private Map<String, Customer> customerDatabase;
    private SimpleDateFormat dateFormat;
    private final String CUSTOMERS_FILE = "customers.txt";
    
    private static class Customer {
        String accountNumber;
        String fullName;
        String address;
        String phoneNumber;
        String email;
        Date dateOfBirth;
        Date accountCreated;
        String customerType; 
        boolean isActive;
        
        public Customer(String accountNumber, String fullName, String address, 
                       String phoneNumber, String email, Date dateOfBirth) {
            this.accountNumber = accountNumber;
            this.fullName = fullName;
            this.address = address;
            this.phoneNumber = phoneNumber;
            this.email = email;
            this.dateOfBirth = dateOfBirth;
            this.accountCreated = new Date();
            this.customerType = "Regular";
            this.isActive = true;
        }
        
        @Override
        public String toString() {
            return String.format("Account: %s | Name: %s | Phone: %s | Type: %s | Active: %s",
                               accountNumber, fullName, phoneNumber, customerType, isActive);
        }
        
        public String toFileString() {
            SimpleDateFormat fileFormat = new SimpleDateFormat("yyyy-MM-dd");
            return String.join("|", 
                accountNumber, 
                fullName, 
                address, 
                phoneNumber, 
                email, 
                fileFormat.format(dateOfBirth),
                fileFormat.format(accountCreated),
                customerType,
                String.valueOf(isActive)
            );
        }
        
        public static Customer fromFileString(String line) {
            try {
                String[] parts = line.split("\\|");
                if (parts.length >= 9) {
                    SimpleDateFormat fileFormat = new SimpleDateFormat("yyyy-MM-dd");
                    
                    Customer customer = new Customer(
                        parts[0], 
                        parts[1], 
                        parts[2], 
                        parts[3], 
                        parts[4], 
                        fileFormat.parse(parts[5]) 
                    );
                    
                    customer.accountCreated = fileFormat.parse(parts[6]);
                    customer.customerType = parts[7];
                    customer.isActive = Boolean.parseBoolean(parts[8]);
                    
                    return customer;
                }
            } catch (Exception e) {
                System.err.println("Error parsing customer data: " + e.getMessage());
            }
            return null;
        }
    }
    
    public CustomerData() {
        customerDatabase = new HashMap<>();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        
        loadCustomerData();
        
        if (customerDatabase.isEmpty()) {
            initializeSampleCustomers();
        }
        
        System.out.println("Customer Data Manager initialized with " + 
                          customerDatabase.size() + " customers");
    }
    
    private void initializeSampleCustomers() {
        try {
            Date dob1 = dateFormat.parse("01/15/1985");
            Date dob2 = dateFormat.parse("03/22/1990");
            Date dob3 = dateFormat.parse("07/08/1988");
            Date dob4 = dateFormat.parse("12/03/1992");
            Date dob5 = dateFormat.parse("09/17/1987");
            
            addCustomer("ACC001", "John Smith", "123 Main St, City, State 12345", 
                       "(555) 123-4567", "john.smith@email.com", dob1);
            
            addCustomer("ACC002", "Jane Johnson", "456 Oak Ave, City, State 12346", 
                       "(555) 234-5678", "jane.johnson@email.com", dob2);
            
            addCustomer("ACC003", "Mike Brown", "789 Pine St, City, State 12347", 
                       "(555) 345-6789", "mike.brown@email.com", dob3);
            
            addCustomer("ACC004", "Sarah Davis", "321 Elm St, City, State 12348", 
                       "(555) 456-7890", "sarah.davis@email.com", dob4);
            
            addCustomer("ACC005", "Tom Wilson", "654 Maple Ave, City, State 12349", 
                       "(555) 567-8901", "tom.wilson@email.com", dob5);
            
            setCustomerType("ACC002", "Premium");
            setCustomerType("ACC004", "VIP");
            
            saveCustomerData();
            System.out.println("Sample customer data created successfully!");
            
        } catch (Exception e) {
            System.err.println("Error creating sample customers: " + e.getMessage());
        }
    }
    
    public boolean addCustomer(String accountNumber, String fullName, String address,
                              String phoneNumber, String email, Date dateOfBirth) {
        
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            System.out.println("Account number cannot be empty");
            return false;
        }
        
        if (fullName == null || fullName.trim().isEmpty()) {
            System.out.println("Customer name cannot be empty");
            return false;
        }
        
        if (customerDatabase.containsKey(accountNumber)) {
            System.out.println("Customer with account " + accountNumber + " already exists");
            return false;
        }
        
        Customer newCustomer = new Customer(accountNumber, fullName, address, 
                                          phoneNumber, email, dateOfBirth);
        customerDatabase.put(accountNumber, newCustomer);
        
        System.out.println("Customer added successfully: " + fullName + " (Account: " + accountNumber + ")");
        return true;
    }
    
    public String getCustomerInfo(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return "Invalid account number";
        }
        
        Customer customer = customerDatabase.get(accountNumber.trim());
        if (customer == null) {
            return "Customer not found";
        }
        
        StringBuilder info = new StringBuilder();
        info.append("=== CUSTOMER INFORMATION ===\n");
        info.append("Account Number: ").append(customer.accountNumber).append("\n");
        info.append("Full Name: ").append(customer.fullName).append("\n");
        info.append("Address: ").append(customer.address).append("\n");
        info.append("Phone: ").append(customer.phoneNumber).append("\n");
        info.append("Email: ").append(customer.email).append("\n");
        info.append("Date of Birth: ").append(dateFormat.format(customer.dateOfBirth)).append("\n");
        info.append("Account Created: ").append(dateFormat.format(customer.accountCreated)).append("\n");
        info.append("Customer Type: ").append(customer.customerType).append("\n");
        info.append("Status: ").append(customer.isActive ? "Active" : "Inactive").append("\n");
        info.append("===========================");
        
        return info.toString();
    }
    
    public String getCustomerName(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return null;
        }
        
        Customer customer = customerDatabase.get(accountNumber.trim());
        if (customer != null) {
            return customer.fullName;
        }
        
        return null;
    }
    
    public String getCustomerPhone(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return null;
        }
        
        Customer customer = customerDatabase.get(accountNumber.trim());
        if (customer != null) {
            return customer.phoneNumber;
        }
        
        return null;
    }
    
    public String getCustomerEmail(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return null;
        }
        
        Customer customer = customerDatabase.get(accountNumber.trim());
        if (customer != null) {
            return customer.email;
        }
        
        return null;
    }
    
    public String getCustomerType(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return "Regular";
        }
        
        Customer customer = customerDatabase.get(accountNumber.trim());
        if (customer != null) {
            return customer.customerType;
        }
        
        return "Regular";
    }
    
    public boolean setCustomerType(String accountNumber, String customerType) {
        if (accountNumber == null || customerType == null) {
            return false;
        }
        
        Customer customer = customerDatabase.get(accountNumber.trim());
        if (customer != null) {
            String[] validTypes = {"Regular", "Premium", "VIP"};
            for (String type : validTypes) {
                if (type.equalsIgnoreCase(customerType)) {
                    customer.customerType = type;
                    System.out.println("Customer type updated to " + type + " for account " + accountNumber);
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public boolean updateCustomerInfo(String accountNumber, String field, String newValue) {
        if (accountNumber == null || field == null || newValue == null) {
            return false;
        }
        
        Customer customer = customerDatabase.get(accountNumber.trim());
        if (customer == null) {
            return false;
        }
        
        switch (field.toLowerCase()) {
            case "name":
                customer.fullName = newValue;
                break;
            case "address":
                customer.address = newValue;
                break;
            case "phone":
                customer.phoneNumber = newValue;
                break;
            case "email":
                customer.email = newValue;
                break;
            default:
                return false;
        }
        
        System.out.println("Updated " + field + " for customer " + accountNumber);
        return true;
    }
    
    public boolean deactivateCustomer(String accountNumber) {
        if (accountNumber == null) {
            return false;
        }
        
        Customer customer = customerDatabase.get(accountNumber.trim());
        if (customer != null) {
            customer.isActive = false;
            System.out.println("Customer deactivated: " + accountNumber);
            return true;
        }
        
        return false;
    }
    
    public boolean activateCustomer(String accountNumber) {
        if (accountNumber == null) {
            return false;
        }
        
        Customer customer = customerDatabase.get(accountNumber.trim());
        if (customer != null) {
            customer.isActive = true;
            System.out.println("Customer activated: " + accountNumber);
            return true;
        }
        
        return false;
    }
    
    public boolean customerExists(String accountNumber) {
        if (accountNumber == null) {
            return false;
        }
        
        return customerDatabase.containsKey(accountNumber.trim());
    }
    
    public boolean isCustomerActive(String accountNumber) {
        if (accountNumber == null) {
            return false;
        }
        
        Customer customer = customerDatabase.get(accountNumber.trim());
        return customer != null && customer.isActive;
    }
    
    public ArrayList<String> getAllCustomers() {
        ArrayList<String> customerList = new ArrayList<>();
        
        for (Customer customer : customerDatabase.values()) {
            customerList.add(customer.toString());
        }
        
        return customerList;
    }
    
    public ArrayList<String> getCustomersByType(String customerType) {
        ArrayList<String> customerList = new ArrayList<>();
        
        for (Customer customer : customerDatabase.values()) {
            if (customer.customerType.equalsIgnoreCase(customerType)) {
                customerList.add(customer.toString());
            }
        }
        
        return customerList;
    }
    
    public String getCustomerStatistics() {
        int totalCustomers = customerDatabase.size();
        int activeCustomers = 0;
        int regularCustomers = 0;
        int premiumCustomers = 0;
        int vipCustomers = 0;
        
        for (Customer customer : customerDatabase.values()) {
            if (customer.isActive) {
                activeCustomers++;
            }
            
            switch (customer.customerType) {
                case "Regular":
                    regularCustomers++;
                    break;
                case "Premium":
                    premiumCustomers++;
                    break;
                case "VIP":
                    vipCustomers++;
                    break;
            }
        }
        
        StringBuilder stats = new StringBuilder();
        stats.append("=== CUSTOMER STATISTICS ===\n");
        stats.append("Total Customers: ").append(totalCustomers).append("\n");
        stats.append("Active Customers: ").append(activeCustomers).append("\n");
        stats.append("Inactive Customers: ").append(totalCustomers - activeCustomers).append("\n");
        stats.append("Regular Customers: ").append(regularCustomers).append("\n");
        stats.append("Premium Customers: ").append(premiumCustomers).append("\n");
        stats.append("VIP Customers: ").append(vipCustomers).append("\n");
        stats.append("==========================");
        
        return stats.toString();
    }
    
    private void loadCustomerData() {
        File file = new File(CUSTOMERS_FILE);
        if (!file.exists()) {
            System.out.println("Customer data file not found. Will create new one.");
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int loadedCount = 0;
            
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Customer customer = Customer.fromFileString(line.trim());
                    if (customer != null) {
                        customerDatabase.put(customer.accountNumber, customer);
                        loadedCount++;
                    }
                }
            }
            
            System.out.println("Loaded " + loadedCount + " customers from file");
            
        } catch (IOException e) {
            System.err.println("Error loading customer data: " + e.getMessage());
        }
    }
    
    public void saveCustomerData() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CUSTOMERS_FILE))) {
            for (Customer customer : customerDatabase.values()) {
                writer.println(customer.toFileString());
            }
            System.out.println("Customer data saved successfully");
        } catch (IOException e) {
            System.err.println("Error saving customer data: " + e.getMessage());
        }
    }
    
    public void printAllCustomers() {
        System.out.println("\n=== ALL CUSTOMERS ===");
        System.out.println("Total customers: " + customerDatabase.size());
        System.out.println();
        
        for (Customer customer : customerDatabase.values()) {
            System.out.println(customer.toString());
        }
        
        System.out.println("====================\n");
    }
    
    public ArrayList<String> searchCustomersByName(String searchName) {
        ArrayList<String> results = new ArrayList<>();
        
        if (searchName == null || searchName.trim().isEmpty()) {
            return results;
        }
        
        String searchTerm = searchName.trim().toLowerCase();
        
        for (Customer customer : customerDatabase.values()) {
            if (customer.fullName.toLowerCase().contains(searchTerm)) {
                results.add(customer.toString());
            }
        }
        
        return results;
    }
    
    public int getCustomerCount() {
        return customerDatabase.size();
    }
    
    public int getActiveCustomerCount() {
        int count = 0;
        for (Customer customer : customerDatabase.values()) {
            if (customer.isActive) {
                count++;
            }
        }
        return count;
    }
}