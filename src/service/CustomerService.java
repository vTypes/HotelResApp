package service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import model.Customer;

public class CustomerService {
    // Singleton instance
    private static CustomerService instance = null;

    // A map to store customers by email
    private final Map<String, Customer> customers = new HashMap<>();

    // Private constructor for singleton pattern
    private CustomerService() {}

    // Method to get the singleton instance of CustomerService
    public static CustomerService getInstance() {
        if (instance == null) {
            instance = new CustomerService();
        }
        return instance;
    }

    // Add a customer to the system
    public void addCustomer(String email, String firstName, String lastName) {
        Customer customer = new Customer(firstName, lastName, email);
        customers.put(email, customer);
    }

    // Retrieve a customer by email
    public Customer getCustomer(String email) {
        return customers.get(email);
    }

    // Get all customers in the system
    public Collection<Customer> getAllCustomers() {
        return customers.values();
    }
}
