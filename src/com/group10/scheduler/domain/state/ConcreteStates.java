package com.group10.scheduler.domain.state;
import com.group10.scheduler.domain.Booking;
import com.group10.scheduler.domain.BookingStatus;
public class ConcreteStates {
    private ConcreteStates (){

    }
    public static class ConfirmedState implements BookingState{
        @Override
        public boolean checkIn (Booking booking){
            booking.setState (new CheckedInState ());
            return true;
        }
        @Override
        public boolean cancel (Booking booking){
            booking.setState (new CancelledState ());
            return true;
        }
        @Override
        public boolean edit (Booking booking, String start, String end){
            if (start== null || end== null || start.compareTo (end)>= 0){
                return false;
            }
            booking.setStartTime (start);
            booking.setEndTime (end);
            return true;
        }
        @Override
        public boolean extend (Booking booking, String until){
            if (until== null || until.compareTo (booking.getEndTime ())<= 0){
                return false;
            }
            booking.setEndTime (until);
            return true;
        }
        @Override
        public void expire (Booking booking){
            booking.forfeitDeposit ();
            booking.setState (new ExpiredState ());
        }
        @Override
        public void complete (Booking booking){
            booking.setState (new CompletedState ());
        }
        @Override
        public BookingStatus getStatus (){
            return BookingStatus.CONFIRMED;
        }
    }
    public static class CheckedInState implements BookingState{
        @Override
        public boolean checkIn (Booking booking){
            return false;
        }
        @Override
        public boolean cancel (Booking booking){
            return false;
        }
        @Override
        public boolean edit (Booking booking, String start, String end){
            return false;
        }
        @Override
        public boolean extend (Booking booking, String until){
            if (until== null || until.compareTo (booking.getEndTime ())<= 0){
                return false;
            }
            booking.setEndTime (until);
            return true;
        }
        @Override
        public void expire (Booking booking){
            //no-op, already checked in
        }
        @Override
        public void complete (Booking booking){
            booking.setState (new CompletedState ());
        }
        @Override
        public BookingStatus getStatus (){
            return BookingStatus.CHECKED_IN;
        }
    }
    public static class CancelledState implements BookingState{
        @Override
        public boolean checkIn (Booking booking){
            return false;
        }
        @Override
        public boolean cancel (Booking booking){
            return false;
        }
        @Override
        public boolean edit (Booking booking, String start, String end){
            return false;
        }
        @Override
        public boolean extend (Booking booking, String until){
            return false;
        }
        @Override
        public void expire (Booking booking){
            //no-op
        }
        @Override
        public void complete (Booking booking){
            // no-op
        }
        @Override
        public BookingStatus getStatus (){
            return BookingStatus.CANCELLED;
        }
    }
    public static class CompletedState implements BookingState{
        @Override
        public boolean checkIn (Booking booking){
            return false;
        }
        @Override
        public boolean cancel (Booking booking){
            return false;
        }
        @Override
        public boolean edit (Booking booking, String start, String end){
            return false;
        }
        @Override
        public boolean extend (Booking booking, String until){
            return false;
        }
        @Override
        public void expire (Booking booking){
            // no-op
        }
        @Override
        public void complete (Booking booking){
            // no-op
        }
        @Override
        public BookingStatus getStatus (){
            return BookingStatus.COMPLETED;
        }
    }
    public static class ExpiredState implements BookingState{
        @Override
        public boolean checkIn (Booking booking){
            return false;
        }
        @Override
        public boolean cancel (Booking booking){
            return false;
        }
        @Override
        public boolean edit (Booking booking, String start, String end){
            return false;
        }
        @Override
        public boolean extend (Booking booking, String until){
            return false;
        }
        @Override
        public void expire (Booking booking){
            // no-op
        }
        @Override
        public void complete (Booking booking){
            // no op
        }
        @Override
        public BookingStatus getStatus (){
            return BookingStatus.EXPIRED;
        }
    }
    /** Reconstructs the right state object from a CSV status string. Used by CsvBookingRepository. */
    public static BookingState fromStatus (BookingStatus status){
        if (status== null){
            throw new IllegalArgumentException ("Booking status cannot be empty.");
        }
        return switch (status) {
            case CONFIRMED-> new ConfirmedState ();
            case CHECKED_IN-> new CheckedInState ();
            case CANCELLED-> new CancelledState ();
            case COMPLETED-> new CompletedState ();
            case EXPIRED-> new ExpiredState ();
            default-> throw new IllegalArgumentException ("Unknown status: " + status);
        };
    }
}
