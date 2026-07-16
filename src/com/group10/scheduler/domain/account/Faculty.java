package com.group10.scheduler.domain.account;
public class Faculty extends UniversityUser {
	private static final double HOURYLY_RATE=30.0;
	//default constructor
	public Faculty(String email,String password,String accountType,String userName, String organizationId) {
		super(email, password, accountType, userName, organizationId);
		   
	}

	public double getHourlyRate() {
		return HOURYLY_RATE;
	}


}
