package model;
import java.util.regex.Pattern;

public class Customer {
    private String firstName;
    private String lastName;
    private String email;

    public Customer(String firstName, String lastName, String email) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format. Email must be in the format: name@domain.extension");
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(email).matches();
    }

    @Override
    public String toString() {
        return "Customer: " + firstName + " " + lastName + ", Email: " + email;
    }
}
