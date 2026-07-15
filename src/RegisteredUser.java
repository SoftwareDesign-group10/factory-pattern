
public abstract class RegisteredUser {
	// Attributes are protected to protect data integrity; getter methods provide controlled access.
	private String email;
	private String password;
	private String userName;
	protected String accountType;
	protected double hourlyRate;
	private long organizationId;
	
	//constructor
	 public RegisteredUser(
	            String email,
	            String password,
	            String accountType,
	            String userName) {

	        this.email = email;
	        this.password = password;
	        this.accountType = accountType;
	        this.userName = userName;
	    }
	//getter methods
	/*password shouldn't be called by anyone.
	 * public String getPassword() {
		return password;
	}*/ 

	public String getEmail() {
		return email;
	}
	public String getUserName() {
		return userName;
	}
	public String getAccountType() {
		return accountType;
	}
	public double getHourlyRate() {
		return hourlyRate;
	}
	
	//will be implemented inside each of each subclass
	protected abstract void setHourlyRate();
	
}
