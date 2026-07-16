package com.group10.scheduler.facade;

import com.group10.scheduler.domain.Administrator;
import com.group10.scheduler.domain.Booking;
import com.group10.scheduler.domain.BookingManager;
import com.group10.scheduler.domain.ChiefEventCoordinator;
import com.group10.scheduler.domain.PaymentMethod;
import com.group10.scheduler.domain.Room;
import com.group10.scheduler.domain.RoomManager;
import com.group10.scheduler.domain.account.AccountManagement;
import com.group10.scheduler.domain.account.RegisteredUser;
import com.group10.scheduler.domain.strategy.ConcreteStrategies;
import com.group10.scheduler.domain.strategy.PaymentStrategy;

import java.util.List;

public class SchedulerFacade {
	private final RoomManager roomManager;
	private final BookingManager bookingManager;
	private final AccountManagement accountManagement;

	public SchedulerFacade(RoomManager roomManager, BookingManager bookingManager,
			AccountManagement accountManagement) {
		this.roomManager = roomManager;
		this.bookingManager = bookingManager;
		this.accountManagement = accountManagement;
	}

	public RegisteredUser createAccount(String email, String password, String accountType, String userName,
			String organizationId) {
		return accountManagement.createAccount(email, password, accountType, userName, organizationId);
	}

	public RegisteredUser login(String email, String password) {
		RegisteredUser user = accountManagement.findByEmail(email);
		if (user != null && user.getPassword().equals(password))
			return user;
		return null;
	}

	public List<Room> getBookableRooms(String start, String end) {
		return bookingManager.getBookableRooms(start, end);
	}

	public Booking bookRoom(String userEmail, String roomId, String start, String end, PaymentMethod method) {
		RegisteredUser user = accountManagement.findByEmail(userEmail);
		Room room = roomManager.findRoomById(roomId);
		PaymentStrategy strategy = ConcreteStrategies.fromMethod(method);
		return bookingManager.bookRoom(user, room, start, end, method, strategy);
	}

	public boolean checkIn(String userEmail, String bookingId) {
		return bookingManager.checkIn(userEmail, bookingId);
	}

	public boolean editBooking(String bookingId, String start, String end) {
		Booking booking = bookingManager.findBooking(bookingId);
		if (booking == null)
			return false;
		boolean ok = booking.editBooking(start, end);
		if (ok) {
			booking.setStartTime(start);
			booking.setEndTime(end);
		}
		bookingManager.persistBookings();
		return ok;
	}

	public boolean cancelBooking(String bookingId) {
		Booking booking = bookingManager.findBooking(bookingId);
		if (booking == null)
			return false;
		boolean ok = booking.cancelBooking();
		bookingManager.persistBookings();
		return ok;
	}

	public boolean extendBooking(String bookingId, String until) {
		Booking booking = bookingManager.findBooking(bookingId);
		if (booking == null)
			return false;
		boolean ok = booking.extendBooking(until);
		if (ok)
			booking.setEndTime(until);
		bookingManager.persistBookings();
		return ok;
	}

	// calling functions from roomManager class
	public void addRoom(Room room) {
		roomManager.addRoom(room);
	}

	public void enableRoom(String roomId) {
		roomManager.enableRoom(roomId);
	}

	public void disableRoom(String roomId) {
		roomManager.disableRoom(roomId);
	}

	public void closeRoom(String roomId) {
		roomManager.closeRoom(roomId);
	}
	//Creating admins by using chief singleton 
	public Administrator generateAdministratorAccount(
	        String adminId,
	        String name,
	        String email) {

	    ChiefEventCoordinator chief =
	            ChiefEventCoordinator.getInstance();

	    return chief.generateAdministratorAccount(
	            adminId,
	            name,
	            email);
	}

	/*
	 * public List<Room> getAllRooms() { return roomManager.getAllRooms(); }
	 */
	/*
	 * public List<Booking> getAllBookings() { return
	 * bookingManager.getAllBookings(); }
	 */
}
