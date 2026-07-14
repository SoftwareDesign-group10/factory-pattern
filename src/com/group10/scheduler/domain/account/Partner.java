package com.group10.scheduler.domain.account;

/** From Farid's factory-pattern repo, unchanged. */
public class Partner extends RegisteredUser {
	public Partner(String email, String password, String accountType, String userName) {
		super(email, password, accountType, userName);
		setHourlyRate();
	}
	protected void setHourlyRate() {
		this.hourlyRate = 50.0;
	}
}
