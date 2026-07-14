package com.group10.scheduler.domain;

public class Room {
    private String roomId;
    private int capacity;
    private String building;
    private String roomNumber;
    private RoomStatus status;
    private RoomSensorSystem sensorSystem;

    public Room(String roomId, int capacity, String building, String roomNumber, RoomStatus status) {
        this.roomId = roomId;
        this.capacity = capacity;
        this.building = building;
        this.roomNumber = roomNumber;
        this.status = status;
        this.sensorSystem = new RoomSensorSystem(roomId + "-sensor");
    }

    public RoomSensorSystem getSensorSystem() { return sensorSystem; }

    public void enable() { this.status = RoomStatus.AVAILABLE; }
    public void disable() { this.status = RoomStatus.DISABLED; }
    public void closeForMaintenance() { this.status = RoomStatus.MAINTENANCE; }
    public boolean isAvailable() { return status == RoomStatus.AVAILABLE; }

    public String getRoomId() { return roomId; }
    public int getCapacity() { return capacity; }
    public String getBuilding() { return building; }
    public String getRoomNumber() { return roomNumber; }
    public RoomStatus getStatus() { return status; }
    public void setStatus(RoomStatus status) { this.status = status; }
}
