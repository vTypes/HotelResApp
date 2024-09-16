package model;

import java.util.Date;

public class Reservation {
    private Customer customer;
    private IRoom room;
    private Date checkInDate;
    private Date checkOutDate;

    public Reservation(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public IRoom getRoom() {
        return room;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    @Override
    public String toString() {
        return "Reservation:\n" +
                "Customer: " + customer.getFirstName() + " " + customer.getLastName() + "\n" +
                "Room: " + room.getRoomNumber() + " - " + room.getRoomType() + "\n" +
                "Price: $" + room.getRoomPrice() + " per night\n" +
                "Check-in Date: " + checkInDate + "\n" +
                "Check-out Date: " + checkOutDate + "\n";
    }
}
