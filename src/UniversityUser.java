
public abstract class UniversityUser extends RegisteredUser {
	//constructor
	public UniversityUser(
            String email,
            String password,
            String userName,
            String accountType) {

        super(email, password, accountType,userName);
    }
}
