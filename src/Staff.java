
public class Staff extends UniversityUser {
	//constructor 
	public Staff(String email,String password,String accountType,String userName) {
		super(email, password, accountType, userName);
		   setHourlyRate();
	}
	//setter method
	protected void setHourlyRate() {
		this.hourlyRate = 40.0;
	}
}
