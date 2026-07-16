package com.group10.scheduler.domain;

public class RoomSensorSystem {
	
	private String sensorId;
    private boolean occupied=false;

    // Simulates receiving occupancy data from a sensor

    public boolean detectOccupancy() {
        return occupied;
    }

    // Simulates scanning a badge
    public boolean scanBadge(String badgeId) {

        if (badgeId == null || badgeId.isBlank()) {
            throw new IllegalArgumentException("Invalid badge ID.");
        }
        this.occupied=true;

        return true;
    }
    public String getSensorId() {
    	return sensorId;
    }
    public void setSensorId(String sensorId) {
    	this.sensorId=sensorId;
    }
    public boolean getOccupied() {
    	return occupied;
    }
    public void setOccupied(boolean occupied) {
    	this.occupied=occupied;
    }
}