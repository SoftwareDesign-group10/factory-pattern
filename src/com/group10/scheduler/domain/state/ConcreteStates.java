package com.group10.scheduler.domain.state;

import com.group10.scheduler.domain.Booking;
import com.group10.scheduler.domain.BookingStatus;

/**
 * Simplified placeholder concrete states — enough for CSV persistence and
 * the GUI to work end-to-end. Replace with the teammate's real State-pattern
 * classes (which should implement the full Req4/Req8/Req9 rules); the rest
 * of the app only calls booking.checkIn()/cancelBooking()/etc, so swapping
 * these out is a drop-in change.
 */
public class ConcreteStates {

    public static class ConfirmedState implements BookingState {
        public boolean checkIn(Booking booking) {
            booking.setState(new CheckedInState());
            return true;
        }
        public boolean cancel(Booking booking) {
            booking.setState(new CancelledState());
            return true;
        }
        public boolean edit(Booking booking, String start, String end) { return true; }
        public boolean extend(Booking booking, String until) { return true; }
        public void expire(Booking booking) { booking.setState(new ExpiredState()); }
        public void complete(Booking booking) { booking.setState(new CompletedState()); }
        public BookingStatus getStatus() { return BookingStatus.CONFIRMED; }
    }

    public static class CheckedInState implements BookingState {
        public boolean checkIn(Booking booking) { return false; }
        public boolean cancel(Booking booking) { return false; }
        public boolean edit(Booking booking, String start, String end) { return false; }
        public boolean extend(Booking booking, String until) { return true; }
        public void expire(Booking booking) { /* no-op, already checked in */ }
        public void complete(Booking booking) { booking.setState(new CompletedState()); }
        public BookingStatus getStatus() { return BookingStatus.CHECKED_IN; }
    }

    public static class CancelledState implements BookingState {
        public boolean checkIn(Booking booking) { return false; }
        public boolean cancel(Booking booking) { return false; }
        public boolean edit(Booking booking, String start, String end) { return false; }
        public boolean extend(Booking booking, String until) { return false; }
        public void expire(Booking booking) { /* no-op */ }
        public void complete(Booking booking) { /* no-op */ }
        public BookingStatus getStatus() { return BookingStatus.CANCELLED; }
    }

    public static class CompletedState implements BookingState {
        public boolean checkIn(Booking booking) { return false; }
        public boolean cancel(Booking booking) { return false; }
        public boolean edit(Booking booking, String start, String end) { return false; }
        public boolean extend(Booking booking, String until) { return false; }
        public void expire(Booking booking) { /* no-op */ }
        public void complete(Booking booking) { /* no-op */ }
        public BookingStatus getStatus() { return BookingStatus.COMPLETED; }
    }

    public static class ExpiredState implements BookingState {
        public boolean checkIn(Booking booking) { return false; }
        public boolean cancel(Booking booking) { return false; }
        public boolean edit(Booking booking, String start, String end) { return false; }
        public boolean extend(Booking booking, String until) { return false; }
        public void expire(Booking booking) { /* no-op */ }
        public void complete(Booking booking) { /* no-op */ }
        public BookingStatus getStatus() { return BookingStatus.EXPIRED; }
    }

    /** Reconstructs the right state object from a CSV status string. Used by CsvBookingRepository. */
    public static BookingState fromStatus(BookingStatus status) {
        switch (status) {
            case CONFIRMED: return new ConfirmedState();
            case CHECKED_IN: return new CheckedInState();
            case CANCELLED: return new CancelledState();
            case COMPLETED: return new CompletedState();
            case EXPIRED: return new ExpiredState();
            default: throw new IllegalArgumentException("Unknown status: " + status);
        }
    }
}
