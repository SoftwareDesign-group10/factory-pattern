package com.group10.scheduler.gui;

import com.group10.scheduler.domain.BookingManager;
import com.group10.scheduler.domain.RoomManager;
import com.group10.scheduler.domain.account.AccountManagement;
import com.group10.scheduler.domain.account.RegisteredUserFactory;
import com.group10.scheduler.facade.SchedulerFacade;
import com.group10.scheduler.persistence.csv.CsvBookingRepository;
import com.group10.scheduler.persistence.csv.CsvPaymentRepository;
import com.group10.scheduler.persistence.csv.CsvRoomRepository;
import com.group10.scheduler.persistence.csv.CsvUserRepository;

import javax.swing.*;
import java.awt.*;

public class MainUI extends JFrame {

    private final GUIController controller;
    private final JTabbedPane tabs = new JTabbedPane();
    private BookingPanel bookingPanel;
    private AdminPanel adminPanel;
    private LoginPanel loginPanel;

    public MainUI(SchedulerFacade facade) {
        super("YorkU Conference Room Scheduler");
        this.controller = new GUIController(facade);
        buildUI();
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void buildUI() {
        loginPanel = new LoginPanel(controller, this::onLoginSuccess);
        bookingPanel = new BookingPanel(controller);
        adminPanel = new AdminPanel(controller);

        tabs.addTab("Login / Register", loginPanel);
        tabs.addTab("Book a Room", bookingPanel);
        tabs.addTab("Admin", adminPanel);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(tabs, BorderLayout.CENTER);
    }

    private void onLoginSuccess() {
        bookingPanel.refreshMyBookings();
        adminPanel.refreshRooms();
        tabs.setSelectedComponent(bookingPanel);
    }

    /**
     * Wires the CSV persistence layer (Adapter pattern), domain managers, and
     * Facade, then launches the GUI. This is the composition root — the only
     * place in the app that mentions concrete CSV classes and file paths.
     */
    public static void main(String[] args) {
        String dataDir = "data"; // relative to the working directory; contains the CSV files

        CsvRoomRepository roomRepo = new CsvRoomRepository(dataDir + "/rooms.csv");
        CsvBookingRepository bookingRepo = new CsvBookingRepository(dataDir + "/bookings.csv");
        CsvUserRepository userRepo = new CsvUserRepository(dataDir + "/users.csv");
        CsvPaymentRepository paymentRepo = new CsvPaymentRepository(dataDir + "/payments.csv");

        RoomManager roomManager = new RoomManager(roomRepo);
        BookingManager bookingManager = new BookingManager(bookingRepo, paymentRepo, roomManager);
        AccountManagement accountManagement = new AccountManagement(userRepo, new RegisteredUserFactory());

        SchedulerFacade facade = new SchedulerFacade(roomManager, bookingManager, accountManagement);

        SwingUtilities.invokeLater(() -> {
            MainUI frame = new MainUI(facade);
            frame.setVisible(true);
        });
    }
}
