package com.group10.scheduler.gui;

import com.group10.scheduler.domain.Booking;
import com.group10.scheduler.domain.PaymentMethod;
import com.group10.scheduler.domain.Room;
import com.group10.scheduler.domain.account.RegisteredUser;
import com.group10.scheduler.facade.SchedulerFacade;

import java.util.List;

/**
 * GUIController — per the team's diagram, this is the only class the GUI's
 * button handlers call into, and it only calls SchedulerFacade (never
 * RoomManager/BookingManager directly), preserving the Facade boundary.
 */
public class GUIController {
    private final SchedulerFacade facade;
    private RegisteredUser currentUser;

    public GUIController(SchedulerFacade facade) {
        this.facade = facade;
    }

    public RegisteredUser getCurrentUser() { return currentUser; }

    public RegisteredUser onLoginClicked(String email, String password) {
        currentUser = facade.login(email, password);
        return currentUser;
    }

    public void onLogoutClicked() {
        currentUser = null;
    }

    public RegisteredUser onRegisterClicked(String email, String password, String userName, String accountType, long id) {
        RegisteredUser user = facade.createAccount(email, password, userName, accountType, id);
        currentUser = user;
        return user;
    }

    public List<Room> onSearchRoomsClicked(String start, String end) {
        return facade.getBookableRooms(start, end);
    }

    public Booking onBookRoomClicked(String roomId, String start, String end, PaymentMethod method) {
        return facade.bookRoom(currentUser.getEmail(), roomId, start, end, method);
    }

    public boolean onCheckInClicked(String bookingId) {
        return facade.checkIn(currentUser.getEmail(), bookingId);
    }

    public boolean onCancelClicked(String bookingId) {
        return facade.cancelBooking(bookingId);
    }

    public boolean onExtendClicked(String bookingId, String until) {
        return facade.extendBooking(bookingId, until);
    }

    public boolean onEditBookingClicked(String bookingId, String start, String end) {
        return facade.editBooking(bookingId, start, end);
    }

    public List<Booking> getMyBookings() {
        if (currentUser == null) return List.of();
        return facade.getAllBookings().stream()
                .filter(b -> b.getUserEmail().equals(currentUser.getEmail()))
                .toList();
    }

    public Room onAddRoomClicked(String roomId, int capacity, String building, String roomNumber) {
        Room room = new Room(roomId, capacity, building, roomNumber, com.group10.scheduler.domain.RoomStatus.AVAILABLE);
        return facade.addRoom(room);
    }

    public boolean onEnableRoomClicked(String roomId) { return facade.enableRoom(roomId); }
    public boolean onDisableRoomClicked(String roomId) { return facade.disableRoom(roomId); }
    public boolean onCloseRoomClicked(String roomId) { return facade.closeRoom(roomId); }

    public List<Room> getAllRooms() { return facade.getAllRooms(); }
}
