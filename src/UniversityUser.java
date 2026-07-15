
public abstract class UniversityUser extends RegisteredUser {

    public UniversityUser(
            String email,
            String password,
            String accountType,
            String userName) {

        super(email, password, accountType, userName);
    }
}