package com.group10.scheduler.domain.account;

/**
 * Factory Method pattern -- from Farid's factory-pattern repo, with fixes/additions
 * marked below. See CHANGES.md for the full list.
 */
public class RegisteredUserFactory {

	// creating a private object of account management
	private AccountValidator m1 = new AccountValidator(); // renamed from AccountManagement, see CHANGES.md

	// using factory pattern to create users -- Farid's original signature, unchanged
	public RegisteredUser createUser(String email, String password, String accountType, String userName) {
		m1.validateAccount(email, password, accountType, userName);
		if (accountType.equalsIgnoreCase("STUDENT")) {
			return new Student(email, password, accountType, userName);
		}
		else if (accountType.equalsIgnoreCase("STAFF")) {
			return new Staff(email, password, accountType, userName);
		}
		else if (accountType.equalsIgnoreCase("FACULTY")) {
			return new Faculty(email, password, accountType, userName);
		}
		else if (accountType.equalsIgnoreCase("PARTNER")) {
			// ADDED -- Partner branch was missing from the original (see CHANGES.md)
			return new Partner(email, password, accountType, userName);
		}
		throw new IllegalArgumentException("Unsupported account type: " + accountType);
	}

	/**
	 * ADDED overload -- not in Farid's original. The diagram's createUser() and Farid's
	 * implementation don't collect a student ID / organization ID anywhere, but the CSV
	 * persistence layer needs one to round-trip user records (Req1/Req3 pricing tiers
	 * reference organization/student IDs). This calls the original createUser() unchanged
	 * and then sets the id via the new setter on RegisteredUser. Team should confirm this
	 * is the right place to collect it -- see CHANGES.md.
	 */
	public RegisteredUser createUser(String email, String password, String accountType, String userName, long organizationId) {
		RegisteredUser user = createUser(email, password, accountType, userName);
		user.setOrganizationId(organizationId);
		return user;
	}
}
