package com.group10.scheduler.domain.account;

/** From Farid's factory-pattern repo, unchanged. */
public class Faculty extends UniversityUser {
	public Faculty(String email, String password, String accountType, String userName) {
		super(email, password, accountType, userName);
		setHourlyRate();
	}
	protected void setHourlyRate() {
		this.hourlyRate = 30.0;
	}
}
