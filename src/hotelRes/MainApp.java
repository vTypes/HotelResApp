package hotelRes;
import service.CustomerService;
import service.ReservationService;
import model.IRoom;
import model.FreeRoom;
import model.RoomType;
import model.Customer;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Calendar;
import java.util.Scanner;

public class MainApp {

    private static final CustomerService customerService = CustomerService.getInstance();
    private static final ReservationService reservationService = ReservationService.getInstance();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            System.out.println("Welcome to the Hotel Reservation Application");
            System.out.println("--------------------------------------------------");
            System.out.println("1. Find and reserve a room");
            System.out.println("2. See my reservations");
            System.out.println("3. Create an Account");
            System.out.println("4. Admin");
            System.out.println("5. Exit");
            System.out.println("--------------------------------------------------");
            System.out.println("Please select a number for the menu option");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    findAndReserveRoom();
                    break;
                case "2":
                    viewMyReservations();
                    break;
                case "3":
                    createAccount();
                    break;
                case "4":
                    adminMenu();
                    break;
                case "5":
                    running = false;
                    System.out.println("Exiting application. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }

    private static void findAndReserveRoom() {
        // Get check-in and check-out dates
        Date[] dates = getCheckInCheckOutDates();
        if (dates != null) {
            Collection<IRoom> availableRooms = reservationService.findRooms(dates[0], dates[1]);
            if (availableRooms.isEmpty()) {
                System.out.println("No rooms available for these dates. How many days later would you like to search for?");
                int daysLater = Integer.parseInt(scanner.nextLine());

                // Adjust dates by the input number of days
                Calendar cal = Calendar.getInstance();
                cal.setTime(dates[0]);
                cal.add(Calendar.DATE, daysLater);
                Date newCheckInDate = cal.getTime();
                cal.setTime(dates[1]);
                cal.add(Calendar.DATE, daysLater);
                Date newCheckOutDate = cal.getTime();

                // Search for rooms in the new date range
                availableRooms = reservationService.findRooms(newCheckInDate, newCheckOutDate);
                if (availableRooms.isEmpty()) {
                    System.out.println("No rooms available on alternative dates.");
                    return;
                } else {
                    System.out.println("Rooms available from " + newCheckInDate + " to " + newCheckOutDate);
                    dates[0] = newCheckInDate;
                    dates[1] = newCheckOutDate;
                }
            }

            // Show available rooms with distinction between free and paid rooms
            System.out.println("Available Rooms:");
            for (IRoom room : availableRooms) {
                System.out.println(room.getRoomNumber() + " - " + (room.isFree() ? "Free" : "Paid, $" + room.getRoomPrice() + " per night"));
            }

            // Ask if they would like to book a room
            System.out.println("Would you like to book a room? (y/n)");
            String bookRoomResponse = scanner.nextLine().trim().toLowerCase();

            if (bookRoomResponse.equals("y")) {
                System.out.println("Enter your email:");
                String email = scanner.nextLine();
                Customer customer = customerService.getCustomer(email);

                if (customer == null) {
                    System.out.println("No customer found with this email. Please create an account first.");
                } else {
                    System.out.println("What room number would you like to reserve?");
                    String roomNumber = scanner.nextLine();
                    IRoom selectedRoom = reservationService.getRoom(roomNumber);

                    if (selectedRoom == null) {
                        System.out.println("Invalid room number.");
                    } else {
                        reservationService.reserveRoom(customer, selectedRoom, dates[0], dates[1]);
                        System.out.println("Room " + selectedRoom.getRoomNumber() + " reserved successfully for " + customer.getFirstName() + " " + customer.getLastName());
                    }
                }
            }
        }
    }

    private static void viewMyReservations() {
        System.out.println("Enter your email:");
        String email = scanner.nextLine();
        Customer customer = customerService.getCustomer(email);

        if (customer != null) {
            for (var reservation : reservationService.getCustomerReservation(customer)) {
                System.out.println(reservation);
            }
        } else {
            System.out.println("No customer found with this email.");
        }
    }

    private static void createAccount() {
        System.out.println("Enter first name:");
        String firstName = scanner.nextLine();
        System.out.println("Enter last name:");
        String lastName = scanner.nextLine();
        System.out.println("Enter email:");
        String email = scanner.nextLine();

        customerService.addCustomer(email, firstName, lastName);
        System.out.println("Customer account created successfully.");
    }

    private static void adminMenu() {
        boolean adminRunning = true;
        while (adminRunning) {
            System.out.println("Admin Menu:");
            System.out.println("1. View All Customers");
            System.out.println("2. View All Rooms");
            System.out.println("3. Add Room");
            System.out.println("4. View All Reservations");
            System.out.println("5. Populate with Test Data");
            System.out.println("6. Go Back to Main Menu");

            String adminChoice = scanner.nextLine();
            switch (adminChoice) {
                case "1":
                    for (Customer customer : customerService.getAllCustomers()) {
                        System.out.println(customer);
                    }
                    break;
                case "2":
                    for (IRoom room : reservationService.findRooms(null, null)) {
                        System.out.println(room);
                    }
                    break;
                case "3":
                    addRoom();
                    break;
                case "4":
                    reservationService.printAllReservation();
                    break;
                case "5":
                    populateTestData();
                    break;
                case "6":
                    adminRunning = false;
                    break;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }

    private static void addRoom() {
        System.out.println("Enter room number:");
        String roomNumber = scanner.nextLine();
        System.out.println("Enter price per night (enter 0 if free room):");
        Double price = Double.parseDouble(scanner.nextLine());
        System.out.println("Enter room type (SINGLE/DOUBLE):");
        RoomType roomType = RoomType.valueOf(scanner.nextLine().toUpperCase());

        IRoom room;
        if (price == 0.0) {
            room = new FreeRoom(roomNumber, roomType);
        } else {
            room = new Room(roomNumber, price, roomType);
        }

        reservationService.addRoom(room);
    }

    private static Date[] getCheckInCheckOutDates() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            // Get the current date
            Date currentDate = new Date();

            System.out.println("Enter check-in date (MM/dd/yyyy):");
            Date checkIn = dateFormat.parse(scanner.nextLine());

            // Check if the check-in date is in the past
            if (checkIn.before(currentDate)) {
                System.out.println("Error: Check-in date cannot be in the past.");
                return null;
            }

            System.out.println("Enter check-out date (MM/dd/yyyy):");
            Date checkOut = dateFormat.parse(scanner.nextLine());

            // Check if the check-out date is before the check-in date
            if (checkOut.before(checkIn)) {
                System.out.println("Error: Check-out date cannot be before check-in date.");
                return null;
            }

            return new Date[]{checkIn, checkOut};
        } catch (Exception e) {
            System.out.println("Invalid date format. Please try again.");
            return null;
        }
    }

    private static void populateTestData() {
        customerService.addCustomer("test1@domain.com", "Test1", "User1");
        customerService.addCustomer("test2@domain.com", "Test2", "User2");

        reservationService.addRoom(new Room("101", 100.0, RoomType.SINGLE));
        reservationService.addRoom(new FreeRoom("102", RoomType.DOUBLE));

        System.out.println("Test data populated.");
    }
}
