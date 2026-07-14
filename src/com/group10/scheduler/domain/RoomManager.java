package com.group10.scheduler.domain;

import com.group10.scheduler.persistence.RoomRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RoomManager {
    private final RoomRepository roomRepository;
    private List<Room> rooms;

    public RoomManager(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
        this.rooms = new ArrayList<>(roomRepository.loadRooms());
    }

    public Room addRoom(Room room) {
        rooms.add(room);
        roomRepository.saveRooms(rooms);
        return room;
    }

    public boolean enableRoom(String roomId) { return updateStatus(roomId, RoomStatus.AVAILABLE); }
    public boolean disableRoom(String roomId) { return updateStatus(roomId, RoomStatus.DISABLED); }
    public boolean closeRoom(String roomId) { return updateStatus(roomId, RoomStatus.MAINTENANCE); }

    private boolean updateStatus(String roomId, RoomStatus status) {
        Room room = findRoom(roomId);
        if (room == null) return false;
        room.setStatus(status);
        roomRepository.saveRooms(rooms);
        return true;
    }

    public Room findRoom(String roomId) {
        return rooms.stream().filter(r -> r.getRoomId().equals(roomId)).findFirst().orElse(null);
    }

    public List<Room> getAvailableRooms() {
        return rooms.stream().filter(Room::isAvailable).collect(Collectors.toList());
    }

    public List<Room> getAllRooms() {
        return rooms;
    }
}
