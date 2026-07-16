package com.group10.scheduler.domain;
import com.group10.scheduler.domain.account.RegisteredUser;
import com.group10.scheduler.domain.state.ConcreteStates;
import com.group10.scheduler.domain.strategy.PaymentStrategy;
import com.group10.scheduler.persistence.BookingRepository;
import com.group10.scheduler.persistence.PaymentRepository;
import java.time.*;
import java.time.format.*;
import java.util.*;
public class BookingManager{
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final RoomManager roomManager;
    private List <Booking> bookings;
    private List <Payment> payments;
    public BookingManager (BookingRepository bookingRepository, PaymentRepository paymentRepository, RoomManager roomManager){
        this.bookingRepository= bookingRepository;
        this.paymentRepository= paymentRepository;
        this.roomManager= roomManager;
        this.bookings= new ArrayList <> (bookingRepository.loadBookings ());
        this.payments= new ArrayList <> (paymentRepository.loadPayments ());
    }
    public Booking findBooking (String bookingId){
        for (Booking booking : bookings){
            if (bookingId.equals (booking.getBookingId ())){
                return booking;
            }
        }
        return null;
    }
    /** Req3/Req4: Creates a booking and charges one hour's fee as a deposit. */
    public Booking bookRoom (RegisteredUser user, Room room, String start, String end, PaymentMethod method, PaymentStrategy strategy){
        LocalDateTime startTime;
        LocalDateTime endTime;
        if (user== null || room== null){
            throw new IllegalArgumentException ("User or room cannot be null.");
        }
        if (method== null || strategy== null){
            throw new IllegalArgumentException("Payment method or strategy cannot be null.");
        }
        try{
            startTime= LocalDateTime.parse (start);
            endTime= LocalDateTime.parse (end);
        } catch (DateTimeParseException exception){
            throw new IllegalArgumentException ("Start and end times must use the correct format.");
        }
        if (!startTime.isBefore (endTime)){
            throw new IllegalArgumentException ("Booking start time must be before end time.");
        }
        if (!room.isAvailable ()){
            throw new IllegalStateException ("Room " + room.getRoomId () + " is not available");
        }
        boolean roomAlreadyBooked = false;
        for (Booking booking : bookings){
            if (booking.getStatus ()== BookingStatus.CANCELLED || booking.getStatus () == BookingStatus.EXPIRED){
                continue;
            }
            if (!booking.getRoomId ().equals (room.getRoomId ())){
                continue;
            }
            if (overlaps(booking.getStartTime (), booking.getEndTime (), start, end)){
                roomAlreadyBooked= true;
                break;
            }
        }
        if (roomAlreadyBooked){
            throw new IllegalStateException ("Room is already booked during the set period of time.");
        }
        double deposit= user.getHourlyRate ();
        //UUID.randomUUID for generating uniquee bookingId
        String bookingId= UUID.randomUUID ().toString ();
        Payment payment= new Payment(UUID.randomUUID ().toString (), bookingId, deposit, method, PaymentStatus.PENDING, LocalDateTime.now ().toString (), strategy);
        //boolean pa;
        if (!payment.processDeposit (deposit)){
            throw new IllegalStateException ("The deposit payment failed.");
        }
        Booking booking= new Booking(bookingId, user.getEmail (), room.getRoomId (), start, end, deposit, new ConcreteStates.ConfirmedState ());
        bookings.add (booking);
        payments.add (payment);
        bookingRepository.saveBookings (bookings);
        paymentRepository.savePayments (payments);
        return booking;
    }
    /** Req4: check-in must happen within 30 minutes of start time, or the deposit is forfeited.
     *  Req5:verifies occupancy and scans the ID badge via the room's sensor system. */
    public boolean checkIn (String userEmail, String bookingId){
        LocalDateTime bookingStart;
        Booking booking= findBooking (bookingId);
        if (booking== null || userEmail== null || !userEmail.equals (booking.getUserEmail ())){
            return false;
        }
        try{
            bookingStart= LocalDateTime.parse (booking.getStartTime ());
        } catch (DateTimeParseException exception){
            return false;
        }
        LocalDateTime checkInDeadline = bookingStart.plusMinutes (30);
        // The user cannot check in before the booking starts
        if (LocalDateTime.now ().isBefore (bookingStart)){
            return false;
        }
        // After 30 minutes, the booking expires and the deposit is lost.
        if (LocalDateTime.now ().isAfter (checkInDeadline)){
            booking.getState ().expire (booking);
            bookingRepository.saveBookings (bookings);
            return false;
        }
        Room room= roomManager.findRoom (booking.getRoomId ());
        if (room== null || room.getSensorSystem ()== null){
            return false;
        }
        boolean sensorApproved= room.getSensorSystem ().detectOccupancy () && room.getSensorSystem ().scanIDBadge (userEmail);
        if (!sensorApproved){
            return false;
        }
        boolean ok= booking.checkIn ();
        if (ok){
            booking.setCheckInTime (LocalDateTime.now ().toString ());
        }
        bookingRepository.saveBookings (bookings);
        return ok;
    }
    /**
     * Saves changes made directly through the Booking State pattern; such as cancellation, editing, or extension.
     */
    public void persistBookings (){
        bookingRepository.saveBookings (bookings);
    }
    /** Req3 and Req9: Returns rooms that are enabled and not already booked during the requested time range. */
    public List <Room> getBookableRooms (String start, String end){
        LocalDateTime startTime;
        LocalDateTime endTime;
        try{
            startTime= LocalDateTime.parse (start);
            endTime= LocalDateTime.parse (end);
        } catch (DateTimeParseException exception){
            return new ArrayList <> ();
        }
        if (!startTime.isBefore (endTime)){
            return new ArrayList <> ();
        }
        List <String> bookedRoomIds= new ArrayList <> ();
        for (Booking booking : bookings){
            if (booking.getStatus ()== BookingStatus.CANCELLED || booking.getStatus ()== BookingStatus.EXPIRED){
                continue;
            }
            if (overlaps (booking.getStartTime (), booking.getEndTime (), start, end)){
                bookedRoomIds.add (booking.getRoomId ());
            }
        }
        List <Room> availableRooms= new ArrayList <> ();
        for (Room room : roomManager.getAvailableRooms ()){
            if (!bookedRoomIds.contains (room.getRoomId ())){
                availableRooms.add (room);
            }
        }
        return availableRooms;
    }
    private boolean overlaps (String start, String end, String start2, String end2){
        LocalDateTime firstStart = LocalDateTime.parse (start);
        LocalDateTime firstEnd= LocalDateTime.parse (end);
        LocalDateTime secondStart= LocalDateTime.parse (start2);
        LocalDateTime secondEnd= LocalDateTime.parse(end2);
        return firstStart.isBefore (secondEnd) && secondStart.isBefore (firstEnd);
    }
    public List<Booking> getAllBookings() {
        return bookings;
    }
}
