package com.group10.scheduler.domain.account;

/**
 * RENAMED from Farid's "AccountManagement" in factory-pattern to "AccountValidator".
 * See CHANGES.md — the project already has a class called AccountManagement
 * (the diagram-level one with createAccount/verifyUniqueEmail/etc that the
 * CSV layer and GUI depend on), so this class was renamed to avoid a
 * same-package name collision. Logic is otherwise unchanged from Farid's original.
 */
public class AccountValidator {

	public AccountValidator() {
	}

	// validating email
	private final String emailFormat = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

	private void validateEmail(String email) {
		if (email == null || email.isBlank()) {
			throw new IllegalArgumentException("Email cannot be empty.");
		}

		if (!email.matches(emailFormat)) {
			throw new IllegalArgumentException("Email format is invalid.");
		}
	}

	private void validatePassword(String password) {
		if (password == null || password.isBlank()) {
			throw new IllegalArgumentException("Password cannot be empty.");
		}

		if (password.length() < 8) {
			throw new IllegalArgumentException("Password must contain at least 8 characters.");
		}
	}

	private void validateUserName(String userName) {
		if (userName == null || userName.isBlank()) {
			throw new IllegalArgumentException("Username cannot be empty.");
		}
	}

	private void validateAccountType(String accountType) {
		if (accountType == null || accountType.isBlank()) {
			throw new IllegalArgumentException("Account type cannot be empty.");
		}

		if (!accountType.equalsIgnoreCase("STUDENT") && !accountType.equalsIgnoreCase("STAFF")
				&& !accountType.equalsIgnoreCase("FACULTY") && !accountType.equalsIgnoreCase("PARTNER")) {
			// NOTE: added PARTNER here. Farid's original validateAccountType() AND
			// RegisteredUserFactory.createUser() both only handled STUDENT/STAFF/FACULTY --
			// Partner accounts (Req1, Req3) weren't supported at all. Added the Partner
			// branch to both classes. See CHANGES.md.
			throw new IllegalArgumentException("Unsupported account type: " + accountType);
		}
	}

	// check everything
	public void validateAccount(String email, String password, String accountType, String userName) {

		validateEmail(email);
		validatePassword(password);
		validateAccountType(accountType);
		validateUserName(userName);
	}
}
