package com.group10.scheduler.test;

import com.group10.scheduler.domain.BookingManager;
import com.group10.scheduler.domain.PaymentMethod;
import com.group10.scheduler.domain.Room;
import com.group10.scheduler.domain.RoomManager;
import com.group10.scheduler.domain.RoomStatus;
import com.group10.scheduler.domain.account.AccountManagement;
import com.group10.scheduler.domain.account.RegisteredUser;
import com.group10.scheduler.domain.account.RegisteredUserFactory;
import com.group10.scheduler.facade.SchedulerFacade;
import com.group10.scheduler.persistence.csv.CsvBookingRepository;
import com.group10.scheduler.persistence.csv.CsvPaymentRepository;
import com.group10.scheduler.persistence.csv.CsvRoomRepository;
import com.group10.scheduler.persistence.csv.CsvUserRepository;

public class SmokeTest {
    public static void main(String[] args) throws Exception {
        String dataDir = "test-data";
        new java.io.File(dataDir).mkdirs();

        // --- Run 1: create everything fresh ---
        run(dataDir);

        // --- Run 2: reload from the CSVs written by run 1, prove persistence round-trips ---
        System.out.println("\n=== RELOADING FROM CSV (simulating app restart) ===");
        CsvRoomRepository roomRepo = new CsvRoomRepository(dataDir + "/rooms.csv");
        CsvBookingRepository bookingRepo = new CsvBookingRepository(dataDir + "/bookings.csv");
        CsvUserRepository userRepo = new CsvUserRepository(dataDir + "/users.csv");
        CsvPaymentRepository paymentRepo = new CsvPaymentRepository(dataDir + "/payments.csv");

        RoomManager roomManager = new RoomManager(roomRepo);
        BookingManager bookingManager = new BookingManager(bookingRepo, paymentRepo, roomManager);
        AccountManagement accountManagement = new AccountManagement(userRepo, new RegisteredUserFactory());

        System.out.println("Rooms reloaded: " + roomManager.getAllRooms().size());
        System.out.println("Bookings reloaded: " + bookingManager.getAllBookings().size());
        System.out.println("Users reloaded: " + accountManagement.getAllUsers().size());
        bookingManager.getAllBookings().forEach(b ->
            System.out.println("  Booking " + b.getBookingId() + " status=" + b.getStatus() + " (state class: " + b.getState().getClass().getSimpleName() + ")")
        );

        System.out.println("\nSMOKE TEST PASSED");
    }

    private static void run(String dataDir) {
        CsvRoomRepository roomRepo = new CsvRoomRepository(dataDir + "/rooms.csv");
        CsvBookingRepository bookingRepo = new CsvBookingRepository(dataDir + "/bookings.csv");
        CsvUserRepository userRepo = new CsvUserRepository(dataDir + "/users.csv");
        CsvPaymentRepository paymentRepo = new CsvPaymentRepository(dataDir + "/payments.csv");

        RoomManager roomManager = new RoomManager(roomRepo);
        BookingManager bookingManager = new BookingManager(bookingRepo, paymentRepo, roomManager);
        AccountManagement accountManagement = new AccountManagement(userRepo, new RegisteredUserFactory());
        SchedulerFacade facade = new SchedulerFacade(roomManager, bookingManager, accountManagement);

        System.out.println("=== Add room ===");
        Room room = facade.addRoom(new Room("R101", 10, "Lassonde", "101", RoomStatus.AVAILABLE));
        System.out.println("Added: " + room.getRoomId() + " status=" + room.getStatus());

        System.out.println("\n=== Register student ===");
        RegisteredUser user = facade.createAccount("test@yorku.ca", "Abcd1234!", "Test Student", "STUDENT", 12345);
        System.out.println("Registered: " + user.getUserName() + " rate=$" + user.getHourlyRate() + "/hr");

        System.out.println("\n=== Search bookable rooms ===");
        var rooms = facade.getBookableRooms("2026-08-01T09:00", "2026-08-01T10:00");
        System.out.println("Found " + rooms.size() + " room(s)");
        assertTrue(rooms.size() == 1, "Expected 1 bookable room");

        System.out.println("\n=== Book room (credit card) ===");
        var booking = facade.bookRoom("test@yorku.ca", "R101", "2026-08-01T09:00", "2026-08-01T10:00", PaymentMethod.CREDIT_CARD);
        System.out.println("Booked: " + booking.getBookingId() + " status=" + booking.getStatus() + " deposit=$" + booking.getDepositAmount());
        assertTrue(booking.getDepositAmount() == 20.0, "Student deposit should be $20");

        System.out.println("\n=== Search again (room should now be excluded) ===");
        var rooms2 = facade.getBookableRooms("2026-08-01T09:00", "2026-08-01T10:00");
        System.out.println("Found " + rooms2.size() + " room(s)");
        assertTrue(rooms2.size() == 0, "Room should be excluded once booked");

        System.out.println("\n=== Check in ===");
        boolean checkedIn = facade.checkIn("test@yorku.ca", booking.getBookingId());
        System.out.println("Check-in result: " + checkedIn);
        assertTrue(checkedIn, "Check-in should succeed");

        System.out.println("\n=== Try to cancel after check-in (should fail — State pattern blocking it) ===");
        boolean cancelled = facade.cancelBooking(booking.getBookingId());
        System.out.println("Cancel result: " + cancelled + " (expected false)");
        assertTrue(!cancelled, "Cancel should be rejected once checked in");
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) throw new AssertionError("FAILED: " + message);
    }
}
