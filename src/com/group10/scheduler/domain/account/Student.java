package com.group10.scheduler.domain.account;

/** From Farid's factory-pattern repo, unchanged. */
public class Student extends UniversityUser {
	public Student(String email, String password, String accountType, String userName) {
	   super(email, password, accountType, userName);
	   setHourlyRate();
	}
	@Override
	protected void setHourlyRate() {
		this.hourlyRate = 20.0;
	}
}
