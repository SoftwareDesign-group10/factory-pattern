package com.group10.scheduler.domain;
import java.util.ArrayList;
import java.util.List;

public class RoomManager {

    private List<Room> rooms = new ArrayList<>();

    public void addRoom(Room room) {

        // Make sure room ID is unique
        for (Room r : rooms) {
            if (r.getRoomId().equals(room.getRoomId())) {
                throw new IllegalArgumentException(
                        "Room ID already exists."
                );
            }
        }

        // Add room to the list
        rooms.add(room);
    }

    // Search for a room using its room ID
    public Room findRoomById(String roomId) {

        for (Room room : rooms) {
            if (room.getRoomId().equals(roomId)) {
                return room;
            }
        }

        throw new IllegalArgumentException(
                "Room does not exist: " + roomId
        );
    }

    public void enableRoom(String roomId) {
        Room room = findRoomById(roomId);
        room.enable();
    }

    public void disableRoom(String roomId) {
        Room room = findRoomById(roomId);
        room.disable();
    }

    public void closeRoom(String roomId) {
        Room room = findRoomById(roomId);
        room.closeForMaintenance();
    }

    public List<Room> getAvailableRooms() {

        List<Room> availableRooms = new ArrayList<>();

        for (Room room : rooms) {
            if (room.isAvailable()) {
                availableRooms.add(room);
            }
        }

        return availableRooms;
    }
}