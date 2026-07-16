package com.group10.scheduler.domain;

import com.group10.scheduler.domain.state.BookingState;

public class Booking {
    private String bookingId;
    private String userEmail;   // FK to RegisteredUser
    private String roomId;      // FK to Room
    private String startTime;   // ISO-8601 string, e.g. 2026-07-14T09:00
    private String endTime;
    private double depositAmount;
    private boolean checkedIn;
    private String checkInTime;
    private BookingState state;

    public Booking(String bookingId, String userEmail, String roomId,
                    String startTime, String endTime, double depositAmount, BookingState state) {
        this.bookingId = bookingId;
        this.userEmail = userEmail;
        this.roomId = roomId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.depositAmount = depositAmount;
        this.state = state;
    }

    public void setState(BookingState state) { this.state = state; }
    public BookingState getState() { return state; }
    public BookingStatus getStatus() { return state.getStatus(); }

    public boolean checkIn() { return state.checkIn(this); }
    public boolean cancelBooking() { return state.cancel(this); }
    public boolean editBooking(String start, String end) { return state.edit(this, start, end); }
    public boolean extendBooking(String until) { return state.extend(this, until); }

    /**
     * Req3: final cost settlement. Placeholder — real calculation needs the
     * actual duration and the user's hourly rate, which live outside
     * Booking. Team should decide: does Booking hold a rate, or does the
     * caller (BookingManager) pass one in?
     */
    public double calculateFinalCost() {
        return depositAmount; // TODO: replace with real duration * hourlyRate calculation
    }

    /** Req4: called when a user doesn't check in within the 30-minute window. */
    public void forfeitDeposit() {
        this.depositAmount = 0;
    }

    // Getters/setters used by CSV layer and GUI
    public String getBookingId() { return bookingId; }
    public String getUserEmail() { return userEmail; }
    public String getRoomId() { return roomId; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public double getDepositAmount() { return depositAmount; }
    public boolean isCheckedIn() { return checkedIn; }
    public void setCheckedIn(boolean checkedIn) { this.checkedIn = checkedIn; }
    public String getCheckInTime() { return checkInTime; }
    public void setCheckInTime(String checkInTime) { this.checkInTime = checkInTime; }
}
