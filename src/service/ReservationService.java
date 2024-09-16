package service;

import java.util.*;
import model.Customer;
import model.IRoom;
import model.Reservation;

public class ReservationService {
    // Singleton instance
    private static ReservationService instance = null;

    // Collections to store rooms and reservations
    private final Map<String, IRoom> rooms = new HashMap<>();
    private final List<Reservation> reservations = new ArrayList<>();

    // Private constructor for singleton pattern
    private ReservationService() {}

    // Method to get the singleton instance of ReservationService
    public static ReservationService getInstance() {
        if (instance == null) {
            instance = new ReservationService();
        }
        return instance;
    }

    // Add a room to the system, ensuring no duplicate room numbers
    public void addRoom(IRoom room) {
        if (rooms.containsKey(room.getRoomNumber())) {
            System.out.println("Error: A room with this number already exists.");
        } else {
            rooms.put(room.getRoomNumber(), room);
            System.out.println("Room " + room.getRoomNumber() + " added successfully.");
        }
    }

    // Get a room by its room number
    public IRoom getRoom(String roomId) {
        return rooms.get(roomId);
    }

    // Reserve a room for a customer
    public Reservation reserveRoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        reservations.add(reservation);
        return reservation;
    }

    // Find available rooms for a given date range
    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        List<IRoom> availableRooms = new ArrayList<>(rooms.values());

        for (Reservation reservation : reservations) {
            if (!(checkOutDate.before(reservation.getCheckInDate()) || checkInDate.after(reservation.getCheckOutDate()))) {
                availableRooms.remove(reservation.getRoom());
            }
        }
        return availableRooms;
    }

    // Get all reservations made by a specific customer
    public Collection<Reservation> getCustomerReservation(Customer customer) {
        List<Reservation> customerReservations = new ArrayList<>();

        for (Reservation reservation : reservations) {
            if (reservation.getCustomer().equals(customer)) {
                customerReservations.add(reservation);
            }
        }
        return customerReservations;
    }

    // Print all reservations
    public void printAllReservation() {
        for (Reservation reservation : reservations) {
            System.out.println(reservation);
        }
    }
}
