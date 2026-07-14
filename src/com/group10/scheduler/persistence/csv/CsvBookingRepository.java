package com.group10.scheduler.persistence.csv;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.group10.scheduler.domain.Booking;
import com.group10.scheduler.domain.BookingStatus;
import com.group10.scheduler.domain.state.ConcreteStates;
import com.group10.scheduler.persistence.BookingRepository;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvBookingRepository implements BookingRepository {

    private final String filePath;
    private static final String[] HEADERS = {
        "bookingId", "userEmail", "roomId", "startTime", "endTime",
        "depositAmount", "checkedIn", "checkInTime", "status"
    };

    public CsvBookingRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Booking> loadBookings() {
        List<Booking> bookings = new ArrayList<>();
        CsvReader reader = null;
        try {
            reader = new CsvReader(filePath);
            reader.readHeaders();
            while (reader.readRecord()) {
                String bookingId = reader.get("bookingId");
                String userEmail = reader.get("userEmail");
                String roomId = reader.get("roomId");
                String startTime = reader.get("startTime");
                String endTime = reader.get("endTime");
                double depositAmount = Double.parseDouble(reader.get("depositAmount"));
                boolean checkedIn = Boolean.parseBoolean(reader.get("checkedIn"));
                String checkInTime = reader.get("checkInTime");
                BookingStatus status = BookingStatus.valueOf(reader.get("status"));

                Booking booking = new Booking(bookingId, userEmail, roomId, startTime, endTime,
                        depositAmount, ConcreteStates.fromStatus(status));
                booking.setCheckedIn(checkedIn);
                booking.setCheckInTime(checkInTime);
                bookings.add(booking);
            }
        } catch (IOException e) {
            System.out.println("No existing bookings.csv found, starting empty: " + e.getMessage());
        } finally {
            if (reader != null) reader.close();
        }
        return bookings;
    }

    @Override
    public void saveBookings(List<Booking> bookings) {
        CsvWriter writer = null;
        try {
            writer = new CsvWriter(new FileWriter(filePath, false), ',');
            for (String h : HEADERS) writer.write(h);
            writer.endRecord();

            for (Booking b : bookings) {
                writer.write(b.getBookingId());
                writer.write(b.getUserEmail());
                writer.write(b.getRoomId());
                writer.write(b.getStartTime());
                writer.write(b.getEndTime());
                writer.write(String.valueOf(b.getDepositAmount()));
                writer.write(String.valueOf(b.isCheckedIn()));
                writer.write(b.getCheckInTime() == null ? "" : b.getCheckInTime());
                writer.write(b.getStatus().name());
                writer.endRecord();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save bookings.csv", e);
        } finally {
            if (writer != null) writer.close();
        }
    }
}
