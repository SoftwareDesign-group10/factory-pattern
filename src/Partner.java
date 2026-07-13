
public class Partner extends RegisteredUser {
	//constructor
	public Partner(String email,String password,String accountType,String userName) {
		super(email, password, accountType, userName);
		   setHourlyRate();
	}
	protected void setHourlyRate() {
		this.hourlyRate = 50.0;
	}
}
