package com.group10.scheduler.gui;

import com.group10.scheduler.domain.Booking;
import com.group10.scheduler.domain.PaymentMethod;
import com.group10.scheduler.domain.Room;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BookingPanel extends JPanel {
    private final GUIController controller;

    private JTextField startField = new JTextField("2026-08-01T09:00", 16);
    private JTextField endField = new JTextField("2026-08-01T10:00", 16);
    private JComboBox<PaymentMethod> methodBox = new JComboBox<>(PaymentMethod.values());
    private JLabel statusLabel = new JLabel(" ");

    private DefaultTableModel roomsModel = new DefaultTableModel(
            new Object[]{"Room ID", "Building", "Room #", "Capacity"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private JTable roomsTable = new JTable(roomsModel);

    private DefaultTableModel myBookingsModel = new DefaultTableModel(
            new Object[]{"Booking ID", "Room", "Start", "End", "Status"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private JTable myBookingsTable = new JTable(myBookingsModel);

    public BookingPanel(GUIController controller) {
        this.controller = controller;
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(10, 10));

        // --- Top: search form ---
        JPanel searchForm = new JPanel();
        searchForm.add(new JLabel("Start (yyyy-MM-ddTHH:mm):"));
        searchForm.add(startField);
        searchForm.add(new JLabel("End:"));
        searchForm.add(endField);
        JButton searchBtn = new JButton("Search Available Rooms");
        searchForm.add(searchBtn);
        searchBtn.addActionListener(e -> handleSearch());

        // --- Middle: results table + book button ---
        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.add(new JScrollPane(roomsTable), BorderLayout.CENTER);
        JPanel bookRow = new JPanel();
        bookRow.add(new JLabel("Payment method:"));
        bookRow.add(methodBox);
        JButton bookBtn = new JButton("Book Selected Room");
        bookRow.add(bookBtn);
        resultsPanel.add(bookRow, BorderLayout.SOUTH);
        bookBtn.addActionListener(e -> handleBook());

        // --- Bottom: my bookings + actions ---
        JPanel myBookingsPanel = new JPanel(new BorderLayout());
        myBookingsPanel.add(new JLabel("My Bookings"), BorderLayout.NORTH);
        myBookingsPanel.add(new JScrollPane(myBookingsTable), BorderLayout.CENTER);

        JPanel actionsRow = new JPanel();
        JButton refreshBtn = new JButton("Refresh");
        JButton checkInBtn = new JButton("Check In");
        JButton cancelBtn = new JButton("Cancel");
        JButton extendBtn = new JButton("Extend To...");
        actionsRow.add(refreshBtn);
        actionsRow.add(checkInBtn);
        actionsRow.add(cancelBtn);
        actionsRow.add(extendBtn);
        myBookingsPanel.add(actionsRow, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> refreshMyBookings());
        checkInBtn.addActionListener(e -> handleCheckIn());
        cancelBtn.addActionListener(e -> handleCancel());
        extendBtn.addActionListener(e -> handleExtend());

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, resultsPanel, myBookingsPanel);
        split.setResizeWeight(0.5);

        add(searchForm, BorderLayout.NORTH);
        add(split, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
    }

    /** Called by MainUI whenever this tab becomes visible / after login. */
    public void refreshMyBookings() {
        myBookingsModel.setRowCount(0);
        List<Booking> mine = controller.getMyBookings();
        for (Booking b : mine) {
            myBookingsModel.addRow(new Object[]{
                    b.getBookingId(), b.getRoomId(), b.getStartTime(), b.getEndTime(), b.getStatus()
            });
        }
    }

    private void handleSearch() {
        try {
            roomsModel.setRowCount(0);
            List<Room> rooms = controller.onSearchRoomsClicked(startField.getText().trim(), endField.getText().trim());
            for (Room r : rooms) {
                roomsModel.addRow(new Object[]{r.getRoomId(), r.getBuilding(), r.getRoomNumber(), r.getCapacity()});
            }
            setStatus(rooms.isEmpty() ? "No rooms available for that window." : rooms.size() + " room(s) found.", false);
        } catch (Exception ex) {
            setStatus("Error: " + ex.getMessage(), true);
        }
    }

    private void handleBook() {
        int row = roomsTable.getSelectedRow();
        if (row < 0) { setStatus("Select a room first.", true); return; }
        String roomId = (String) roomsModel.getValueAt(row, 0);
        try {
            Booking b = controller.onBookRoomClicked(roomId, startField.getText().trim(), endField.getText().trim(),
                    (PaymentMethod) methodBox.getSelectedItem());
            setStatus("Booked! Booking ID: " + b.getBookingId(), false);
            refreshMyBookings();
        } catch (Exception ex) {
            setStatus("Error: " + ex.getMessage(), true);
        }
    }

    private void handleCheckIn() {
        withSelectedBooking(bookingId -> {
            boolean ok = controller.onCheckInClicked(bookingId);
            setStatus(ok ? "Checked in." : "Check-in rejected (outside window or invalid state).", !ok);
            refreshMyBookings();
        });
    }

    private void handleCancel() {
        withSelectedBooking(bookingId -> {
            boolean ok = controller.onCancelClicked(bookingId);
            setStatus(ok ? "Booking cancelled." : "Cancel rejected.", !ok);
            refreshMyBookings();
        });
    }

    private void handleExtend() {
        withSelectedBooking(bookingId -> {
            String until = JOptionPane.showInputDialog(this, "Extend end time to (yyyy-MM-ddTHH:mm):");
            if (until == null || until.isBlank()) return;
            boolean ok = controller.onExtendClicked(bookingId, until.trim());
            setStatus(ok ? "Booking extended." : "Extend rejected.", !ok);
            refreshMyBookings();
        });
    }

    private void withSelectedBooking(java.util.function.Consumer<String> action) {
        int row = myBookingsTable.getSelectedRow();
        if (row < 0) { setStatus("Select one of your bookings first.", true); return; }
        String bookingId = (String) myBookingsModel.getValueAt(row, 0);
        action.accept(bookingId);
    }

    private void setStatus(String text, boolean isError) {
        statusLabel.setText(text);
        statusLabel.setForeground(isError ? Color.RED : new Color(0, 128, 0));
    }
}
