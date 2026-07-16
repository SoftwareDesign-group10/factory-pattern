package com.group10.scheduler.domain.room;
public class Room {
	private String roomId;
	private int capacity;
	private String building;
	private String roomNumber;

// Making enum for room status
	public enum RoomStatus {
		AVAILABLE, DISABLED, MAINTENANCE, OCCUPIED
	}

	private RoomStatus status;

// Constructor
// Add sensorSystem
	public Room(String roomId, int capacity, String building, String roomNumber) {
		this.roomId = roomId;
		this.capacity = capacity;
		this.building = building;
		this.roomNumber = roomNumber;
		this.status = RoomStatus.AVAILABLE;
	}

// Changing the status of the room
	public void enable() {
		status = RoomStatus.AVAILABLE;
	}

	public void disable() {
		status = RoomStatus.DISABLED;
	}

	public void closeForMaintenance() {
		status = RoomStatus.MAINTENANCE;
	}

	public boolean isAvailable() {
		return (status == RoomStatus.AVAILABLE);
	}

// Getter and setter methods
	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}
}