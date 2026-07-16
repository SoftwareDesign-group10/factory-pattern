package com.group10.scheduler.domain.account;

public class Partner extends RegisteredUser {
	private static final double HOURYLY_RATE = 50.0;

	// constructor
	public Partner(String email, String password, String accountType, String userName,String organizationId) {
		super(email, password, accountType, userName,organizationId);
	}

	public double getHourlyRate() {
		return HOURYLY_RATE;
	}
}
