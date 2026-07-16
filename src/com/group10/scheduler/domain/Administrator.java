package com.group10.scheduler.domain;
public class Administrator {
private String adminId;
private String name;
private String email;
private RoomManager roomManager;

//package private Constructor only will be called by chief 
Administrator(String adminId,String name,String email){
	this.adminId=adminId;
	this.name=name;
	this.email=email;
	roomManager = new RoomManager ();
}
//setters and getters
public String getAdminId() {
	return adminId;
}
public void setAdminId(String adminId) {
	this.adminId = adminId;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
//calling functions from room manager class 
public void addRoom(Room r) {
	roomManager.addRoom(r);
}

public void enableRoom(String roomId) {
    roomManager.enableRoom(roomId);
}

public void disableRoom(String roomId) {
    roomManager.disableRoom(roomId);
}

public void closeRoom(String roomId) {
    roomManager.closeRoom(roomId);
}
}
