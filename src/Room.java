public class Room {

    private String roomId;
    private int capacity;
    private String building;
    private String roomNumber;
    private RoomStatus status;

    private RoomSensorSystem sensorSystem;

    public enum RoomStatus {
        AVAILABLE,
        DISABLED,
        MAINTENANCE,
        OCCUPIED
    }

    public Room(
            String roomId,
            int capacity,
            String building,
            String roomNumber,
            RoomSensorSystem sensorSystem) {

        this.roomId = roomId;
        this.capacity = capacity;
        this.building = building;
        this.roomNumber = roomNumber;
        this.sensorSystem = sensorSystem;

        this.status = RoomStatus.AVAILABLE;
    }

    /*
     * Reads the latest sensor value and updates the room status.
     */
    public void updateOccupancyStatus() {

        // Do not overwrite administrative statuses.
        if (status == RoomStatus.DISABLED ||
            status == RoomStatus.MAINTENANCE) {

            return;
        }

        if (sensorSystem.detectOccupancy()) {
            status = RoomStatus.OCCUPIED;
        } else {
            status = RoomStatus.AVAILABLE;
        }
    }

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
        return status == RoomStatus.AVAILABLE;
    }

    public boolean isOccupied() {
        return status == RoomStatus.OCCUPIED;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public RoomSensorSystem getSensorSystem() {
        return sensorSystem;
    }

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