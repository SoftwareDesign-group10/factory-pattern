package com.group10.scheduler.domain.account;
public class Student extends UniversityUser {
	private static final double HOURYLY_RATE = 20.0;
//constructor
	public Student(String email,String password,String accountType,String userName){
	   super(email, password, accountType, userName);
	}
	public double getHourlyRate() {
		return HOURYLY_RATE;
	}
}

