package com.group10.scheduler.domain.account;
public class Student extends UniversityUser {
//constructor
	public Student(String email,String password,String accountType,String userName){
	   super(email, password, accountType, userName);
	   setHourlyRate();
	}
	@Override
	public void setHourlyRate() {
		this.hourlyRate = 20.0;
	}
}
