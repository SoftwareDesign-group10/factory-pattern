package com.group10.scheduler.domain.account;

public class Staff extends UniversityUser {
	private static final double HOURYLY_RATE = 40.0;

	// constructor
	public Staff(String email, String password, String accountType, String userName, String organizationId) {
		super(email, password, accountType, userName, organizationId);
	}

	public double getHourlyRate() {
		return HOURYLY_RATE;
	}
}
