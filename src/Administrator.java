
public class Administrator {
private long adminId;
private String name;
private String email;

//Constructor
Administrator(long adminId,String name,String email){
	this.adminId=adminId;
	this.name=name;
	this.email=email;
}
//setters and getters
public long getAdminId() {
	return adminId;
}
public void setAdminId(long adminId) {
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
public void addRoom(RoomManager m,Room r) {
	m.addRoom(r);
}
public void enableRoom(RoomManager m,Room r) {
	m.enableRoom(r.getRoomId());
}
public void disabledRoom(RoomManager m,Room r) {
	m.disabledRoom(r.getRoomId());
}
public void closeRoom(RoomManager m,Room r) {
	m.closeRoom(r.getRoomId());
}



}
