package hotelRes;

import model.Customer;
import model.Reservation;
import model.RoomType;

import java.util.*;

public class Hotel {
    private List<Customer> customers = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();

    public void addCustomer(String firstName, String lastName, String email) {
        try {
            // The Customer constructor handles email validation
            Customer customer = new Customer(firstName, lastName, email);
            if (!customers.contains(customer)) {
                customers.add(customer);
                System.out.println("Customer account created.");
            } else {
                System.out.println("Customer with this email already exists.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage()); // Handle the invalid email format
        }
    }

    public Customer findCustomerByEmail(String email) {
        for (Customer customer : customers) {
            if (customer.isValidEmail(email) && customer.toString().contains(email)) {
                return customer;
            }
        }
        return null;
    }

    public void addRoom(int roomNumber, double price, RoomType roomType) {
        String roomNumberStr = String.valueOf(roomNumber); // Convert int to String
        Room room = new Room(roomNumberStr, price, roomType);
        if (!rooms.contains(room)) {
            rooms.add(room);
            System.out.println("Room added.");
        } else {
            System.out.println("Room with this number already exists.");
        }
    }

    public void viewAllRooms() {
        for (Room room : rooms) {
            System.out.println(room);
        }
    }

    public void viewAllCustomers() {
        for (Customer customer : customers) {
            System.out.println(customer);
        }
    }

    public List<Room> searchRooms(Date checkIn, Date checkOut) {
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : rooms) {
            boolean isAvailable = true;
            for (Reservation reservation : reservations) {
                if (reservation.getRoom().equals(room)) {
                    if (!(checkOut.before(reservation.getCheckInDate()) || checkIn.after(reservation.getCheckOutDate()))) {
                        isAvailable = false;
                        break;
                    }
                }
            }
            if (isAvailable) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    public void bookRoom(Customer customer, Room room, Date checkIn, Date checkOut) {
        Reservation reservation = new Reservation(customer, room, checkIn, checkOut);
        reservations.add(reservation);
        System.out.println("Room booked successfully.");
    }

    public void viewReservationsForCustomer(Customer customer) {
        for (Reservation reservation : reservations) {
            if (reservation.getCustomer().equals(customer)) {
                System.out.println(reservation);
            }
        }
    }

    public void viewAllReservations() {
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            for (Reservation reservation : reservations) {
                System.out.println(reservation);
            }
        }
    }
}
