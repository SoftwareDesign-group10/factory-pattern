package com.group10.scheduler.domain.room;
public class RoomSensorSystem {
private String sesnsorId;

//data derived from sensor
private boolean occupancyDetected;

public RoomSensorSystem(String sensorId) {
	this.sesnsorId=sensorId;
	this.occupancyDetected=false;
}
public void receiveOccupancyData(boolean occupancyDetected) {
	this.occupancyDetected = occupancyDetected;
}

/*
 * Placeholder for the future badge-scanner implementation.
 */
public boolean scanIdBadge(long userId) {
	return userId<0;
}
//Setters and Getters
public String getSesnsorId() {
	return sesnsorId;
}

public void setSesnsorId(String sesnsorId) {
	this.sesnsorId = sesnsorId;
}
}
