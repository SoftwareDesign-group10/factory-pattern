package com.group10.scheduler.persistence;

import com.group10.scheduler.domain.Room;
import java.util.List;

public interface RoomRepository {
    List<Room> loadRooms();
    void saveRooms(List<Room> rooms);
}
