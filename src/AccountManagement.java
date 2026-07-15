import java.util.HashSet;
import java.util.Set;

public class AccountManagement {
	private final Set<String> registeredEmails = new HashSet<>();
	//calling the factory method to create account 
	private final RegisteredUserFactory factory;

	// constructor
	public AccountManagement() {
	    factory = new RegisteredUserFactory();
	}

	// creating an enum for account types with flexibility of adding more types
	public enum AccountType {

		STUDENT(true), STAFF(true), FACULTY(true),
		// partner is not a university account
		PARTNER(false);

		private final boolean universityVerificationRequired;

		AccountType(boolean universityVerificationRequired) {
			this.universityVerificationRequired = universityVerificationRequired;
		}

		public boolean requiresUniversityVerification() {
			return universityVerificationRequired;
		}
	}

	// validating email
	private final String emailFormat = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

	private void validateEmail(String email) {

		if (email == null || email.isBlank()) {
			throw new IllegalArgumentException("Email cannot be empty.");
		}

		String normalizedEmail = email.trim().toLowerCase();

		if (!normalizedEmail.matches(emailFormat)) {
			throw new IllegalArgumentException("Email format is invalid.");
		}

		if (registeredEmails.contains(normalizedEmail)) {
			throw new IllegalArgumentException("This email is already registered.");
		}
	}

	private void validatePassword(String password) {

		if (password == null || password.isBlank()) {
			throw new IllegalArgumentException("Password cannot be empty.");
		}

		if (password.length() < 8) {
			throw new IllegalArgumentException("Password must contain at least 8 characters.");
		}

		if (!password.matches(".*[A-Z].*")) {
			throw new IllegalArgumentException("Password must contain at least one uppercase letter.");
		}

		if (!password.matches(".*[a-z].*")) {
			throw new IllegalArgumentException("Password must contain at least one lowercase letter.");
		}

		if (!password.matches(".*\\d.*")) {
			throw new IllegalArgumentException("Password must contain at least one number.");
		}

		if (!password.matches(".*[^A-Za-z0-9].*")) {
			throw new IllegalArgumentException("Password must contain at least one symbol.");
		}
	}

	private void validateUserName(String userName) {
		if (userName == null || userName.isBlank()) {
			throw new IllegalArgumentException("Username cannot be empty.");
		}
	}

	// verify university accounts
	private void verifyUniversityAccount(String email) {

		if (!email.toLowerCase().endsWith("@yorku.ca")) {
			throw new IllegalArgumentException("University accounts must use a university email.");
		}
	}

	/*
	 * private void validateAccountType(String accountType) { if (accountType ==
	 * null || accountType.isBlank()) { throw new
	 * IllegalArgumentException("Account type cannot be empty."); }
	 * 
	 * if (!accountType.equalsIgnoreCase("STUDENT") &&
	 * !accountType.equalsIgnoreCase("STAFF") &&
	 * !accountType.equalsIgnoreCase("FACULTY") &&
	 * !accountType.equalsIgnoreCase("PARTNER")) {
	 * 
	 * throw new IllegalArgumentException("Unsupported account type: " +
	 * accountType); } }
	 */
	private AccountType validateAccountType(String accountType) {

		if (accountType == null || accountType.isBlank()) {
			throw new IllegalArgumentException("Account type cannot be empty.");
		}

		try {
			return AccountType.valueOf(accountType.trim().toUpperCase());

		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Unsupported account type: " + accountType);
		}
	}

	// checks everything
	public void validateAccount(
	        String email,
	        String password,
	        String accountType,
	        String userName) {

	    validateEmail(email);
	    validatePassword(password);
	    validateUserName(userName);

	    AccountType type = validateAccountType(accountType);

	    if (type.requiresUniversityVerification()) {
	        verifyUniversityAccount(email);
	    }

	    registeredEmails.add(email.trim().toLowerCase());
	}
	public RegisteredUser createAccount(
	        String email,
	        String password,
	        String accountType,
	        String userName) {

	    validateAccount(
	            email,
	            password,
	            accountType,
	            userName
	    );

	    return factory.createUser(
	            email.trim().toLowerCase(),
	            password,
	            accountType.trim().toUpperCase(),
	            userName.trim()
	    );
	}
}
