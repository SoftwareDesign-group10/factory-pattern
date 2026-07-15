
public class Faculty extends UniversityUser {
	//default constructor
	public Faculty(String email,String password,String accountType,String userName) {
		super(email, password, accountType, userName);
		   setHourlyRate();
	}
	public void setHourlyRate() {
		this.hourlyRate = 30.0;
	}


}
