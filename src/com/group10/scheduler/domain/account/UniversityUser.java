package com.group10.scheduler.domain.account;

/** From Farid's factory-pattern repo — constructor order bug fixed. See CHANGES.md. */
public abstract class UniversityUser extends RegisteredUser {
	//constructor
	public UniversityUser(
            String email,
            String password,
            String accountType,
            String userName) {

        super(email, password, accountType, userName);
    }
}
