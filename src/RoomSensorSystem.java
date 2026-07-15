public class RoomSensorSystem {

    private String sensorId;

    // Latest data received from the physical sensor
    private boolean occupancyDetected;

    public RoomSensorSystem(String sensorId) {
        this.sensorId = sensorId;
        this.occupancyDetected = false;
    }

    /*
     * This method will be called later when actual sensor data
     * is received by the system.
     */
    public void receiveOccupancyData(boolean occupancyDetected) {
        this.occupancyDetected = occupancyDetected;
    }

    /*
     * Returns the latest occupancy reading.
     */
    public boolean detectOccupancy() {
        return occupancyDetected;
    }

    /*
     * Placeholder for the future badge-scanner implementation.
     */
    public boolean scanIdBadge(long userId) {
        return userId > 0;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }
}