package com.group10.scheduler.domain;

import com.group10.scheduler.domain.account.RegisteredUser;
import com.group10.scheduler.domain.state.ConcreteStates;
import com.group10.scheduler.domain.strategy.PaymentStrategy;
import com.group10.scheduler.persistence.BookingRepository;
import com.group10.scheduler.persistence.PaymentRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BookingManager {
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final RoomManager roomManager;
    private List<Booking> bookings;
    private List<Payment> payments;

    public BookingManager(BookingRepository bookingRepository, PaymentRepository paymentRepository, RoomManager roomManager) {
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
        this.roomManager = roomManager;
        this.bookings = new ArrayList<>(bookingRepository.loadBookings());
        this.payments = new ArrayList<>(paymentRepository.loadPayments());
    }

    public Booking findBooking(String bookingId) {
        return bookings.stream().filter(b -> b.getBookingId().equals(bookingId)).findFirst().orElse(null);
    }

    /** Req3/Req4: books the room and charges one hour's deposit upfront via the given payment strategy. */
    public Booking bookRoom(RegisteredUser user, Room room, String start, String end, PaymentMethod method, PaymentStrategy strategy) {
        if (!room.isAvailable()) {
            throw new IllegalStateException("Room is not available: " + room.getRoomId());
        }
        double deposit = user.getHourlyRate();
        String bookingId = UUID.randomUUID().toString();

        Booking booking = new Booking(bookingId, user.getEmail(), room.getRoomId(),
                start, end, deposit, new ConcreteStates.ConfirmedState());
        bookings.add(booking);
        bookingRepository.saveBookings(bookings);

        Payment payment = new Payment(UUID.randomUUID().toString(), bookingId, deposit, method,
                PaymentStatus.PENDING, LocalDateTime.now().toString(), strategy);
        payment.processDeposit(deposit);
        payments.add(payment);
        paymentRepository.savePayments(payments);

        return booking;
    }

    /** Req4: check-in must happen within 30 minutes of start time, or the deposit is forfeited.
     *  Req5: also verifies occupancy + scans the ID badge via the room's sensor system. */
    public boolean checkIn(String userEmail, String bookingId) {
        Booking booking = findBooking(bookingId);
        if (booking == null || !booking.getUserEmail().equals(userEmail)) return false;

        Room room = roomManager.findRoom(booking.getRoomId());
        boolean sensorOk = room == null || (room.getSensorSystem().detectOccupancy()
                && room.getSensorSystem().scanIDBadge(userEmail));
        if (!sensorOk) return false;

        boolean ok = booking.checkIn();
        if (ok) {
            booking.setCheckedIn(true);
            booking.setCheckInTime(LocalDateTime.now().toString());
        }
        bookingRepository.saveBookings(bookings);
        return ok;
    }

    /**
     * Per the diagram, cancel/edit/extend live on Booking itself (via the
     * State pattern), not on BookingManager. The Facade fetches the Booking
     * via findBooking(), calls the operation directly, then calls this to
     * persist the change — since the in-memory `bookings` list already
     * holds a reference to the same mutated object.
     */
    public void persistBookings() {
        bookingRepository.saveBookings(bookings);
    }

    /** Req3/Req9: rooms that are AVAILABLE and have no active (non-cancelled) booking overlapping [start,end). */
    public List<Room> getBookableRooms(String start, String end) {
        List<String> bookedRoomIds = bookings.stream()
                .filter(b -> b.getStatus() != BookingStatus.CANCELLED && b.getStatus() != BookingStatus.EXPIRED)
                .filter(b -> overlaps(b.getStartTime(), b.getEndTime(), start, end))
                .map(Booking::getRoomId)
                .collect(Collectors.toList());

        return roomManager.getAvailableRooms().stream()
                .filter(r -> !bookedRoomIds.contains(r.getRoomId()))
                .collect(Collectors.toList());
    }

    private boolean overlaps(String startA, String endA, String startB, String endB) {
        // ISO-8601 strings compare lexicographically the same as chronologically.
        return startA.compareTo(endB) < 0 && startB.compareTo(endA) < 0;
    }

    public List<Booking> getAllBookings() {
        return bookings;
    }
}
