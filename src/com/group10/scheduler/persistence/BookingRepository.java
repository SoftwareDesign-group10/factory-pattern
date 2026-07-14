package com.group10.scheduler.persistence;

import com.group10.scheduler.domain.Booking;
import java.util.List;

public interface BookingRepository {
    List<Booking> loadBookings();
    void saveBookings(List<Booking> bookings);
}
