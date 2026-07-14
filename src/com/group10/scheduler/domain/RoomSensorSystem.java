package com.group10.scheduler.domain;

/**
 * Per the diagram: one RoomSensorSystem per Room (1-to-1), used during
 * check-in to verify occupancy and scan the user's ID badge (Req5).
 * Stubbed to always succeed — replace detectOccupancy()/scanIDBadge() with
 * real hardware/simulation logic when that's defined.
 */
public class RoomSensorSystem {
    private String sensorID;

    public RoomSensorSystem(String sensorID) {
        this.sensorID = sensorID;
    }

    public boolean detectOccupancy() {
        return true; // placeholder — no real sensor hardware to simulate
    }

    public boolean scanIDBadge(String userID) {
        return true; // placeholder
    }

    public String getSensorID() { return sensorID; }
}
