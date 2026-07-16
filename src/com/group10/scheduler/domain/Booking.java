package com.group10.scheduler.domain;
import com.group10.scheduler.domain.state.BookingState;
import java.time.*;
import java.util.*;
public class Booking{
    private String bookingId;
    private String userEmail;   // FK to RegisteredUser
    private String roomId;      // FK to Room
    // ISO-8601 string, e.g. 2026-07-14T09:00
    private String startTime;
    private String endTime;
    // Req4; for calculating the total booking cost.
    private double depositAmount;
    //private boolean depositForfeited;
    private boolean checkedIn;
    private String checkInTime;
    // State pattern
    private BookingState state;
    /**
     * Main constructor used when creating a new booking
     */
    public Booking (String bookingId, String userEmail, String roomId, String startTime, String endTime, double depositAmount, BookingState state){
        this.bookingId= bookingId;
        this.userEmail= userEmail;
        this.roomId= roomId;
        this.startTime= startTime;
        this.endTime= endTime;
        this.depositAmount= depositAmount;
        this.state= state;
    }
    public void setState (BookingState state){
        if (state== null){
            throw new IllegalArgumentException ("Booking state cannot be null.");
        }
        this.state = state;
    }
    public BookingState getState (){
        return state;
    }
    public BookingStatus getStatus (){
        return state.getStatus ();
    }
    public boolean checkIn (){
        boolean successful= state.checkIn (this);
        if (successful){
            this.checkedIn= true;
        }
        return successful;
    }
    public boolean cancelBooking (){
        return state.cancel (this);
    }
    public boolean editBooking (String start, String end){
        return state.edit (this, start, end);
    }
    public boolean extendBooking (String until){
        return state.extend (this, until);
    }
    /**
     * Req3 calculates the complete price of the booking.
     * Because Req4 defines the deposit as one hour's fee, depositAmount is used as the hourly rate.
     */
    public double calculateFinalCost (){
        LocalDateTime start= LocalDateTime.parse (startTime);
        LocalDateTime end= LocalDateTime.parse (endTime);
        double durationHours= Duration.between (start, end).toMinutes ()/ 60.0;
        if (durationHours <= 0){
            return 0.0;
        }
        //double durationHours = durationMinutes / 60.0;
        return durationHours * depositAmount;
    }
    /**
     * Req4: Returns the amount still owed after applying the deposit When the user checks in.
     */
    /*public double calculateRemainingBalance (){
        double finalCost= calculateFinalCost ();
        if (depositForfeited || !checkedIn){
            return finalCost;
        }
        return finalCost - depositAmount;
    }*/
    /** Req4: called when a user doesn't check in within the 30 minutes window. */
    public void forfeitDeposit (){
        this.depositAmount = 0;
    }
    // Getters and setters used by persistence and GUI
    public String getBookingId (){
        return bookingId;
    }
    public String getUserEmail (){
        return userEmail;
    }
    public String getRoomId (){
        return roomId;
    }
    public String getStartTime (){
        return startTime;
    }
    public void setStartTime (String startTime){
        this.startTime= startTime;
    }
    public String getEndTime (){
        return endTime;
    }
    public void setEndTime (String endTime){
        this.endTime= endTime;
    }
    public double getDepositAmount (){
        return depositAmount;
    }
    public boolean isCheckedIn (){
        return checkedIn;
    }
    public void setCheckedIn (boolean checkedIn){
        this.checkedIn= checkedIn;
    }
    public String getCheckInTime (){
        return checkInTime;
    }
    public void setCheckInTime (String checkInTime){
        this.checkInTime= checkInTime;
    }
    /*public boolean isDepositForfeited (){
        return depositForfeited;
    }
    public void setDepositForfeited (boolean depositForfeited){
        this.depositForfeited= depositForfeited;
    }*/
}
