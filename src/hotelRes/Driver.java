package hotelRes;

import model.Customer;

public class Driver {
    public static void main(String[] args) {
        // Test Case 1: Creating a Customer with valid data
        try {
            Customer customer = new Customer("first", "second", "joe@domain.com");
            System.out.println(customer);  // This should print customer details if toString() is properly overridden
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Test Case 2: Creating a Customer with an invalid email
        try {
            Customer invalidCustomer = new Customer("first", "second", "email");
            System.out.println(invalidCustomer);  // Should not reach this line
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());  // This should catch the exception for invalid email
        }
    }
}