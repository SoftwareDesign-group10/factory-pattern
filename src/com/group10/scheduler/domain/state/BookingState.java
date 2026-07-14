package com.group10.scheduler.domain.state;

import com.group10.scheduler.domain.Booking;
import com.group10.scheduler.domain.BookingStatus;

/**
 * State pattern interface.
 * NOTE: This is a placeholder shape matching the team's class diagram.
 * Swap in the teammate's real implementation when it's ready — the CSV
 * layer and GUI only depend on getStatus(), so nothing else should break.
 */
public interface BookingState {
    boolean checkIn(Booking booking);
    boolean cancel(Booking booking);
    boolean edit(Booking booking, String start, String end);
    boolean extend(Booking booking, String until);
    void expire(Booking booking);
    void complete(Booking booking);
    BookingStatus getStatus();
}
