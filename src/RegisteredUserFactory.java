
public class RegisteredUserFactory {
	
	// creating a private object of account management 
	private AccountManagement m1 = new AccountManagement();
	
	
	//using factory pattern to create users
	public RegisteredUser createUser(String email, String password,String accountType, String userName) {
		m1.validateAccount(
                email,
                password,
                accountType,
                userName);
		if(accountType.equalsIgnoreCase("STUDENT")) {
			return new Student(email, password,accountType,userName);
		}
		else if(accountType.equalsIgnoreCase("STAFF")) {
			return new Staff(email, password,accountType,userName);
		}
		else if(accountType.equalsIgnoreCase("FACULTY")) {
			return new Faculty(email, password,accountType,userName);
		}
		throw new IllegalArgumentException(
                "Unsupported account type: " + accountType);
	}
}
