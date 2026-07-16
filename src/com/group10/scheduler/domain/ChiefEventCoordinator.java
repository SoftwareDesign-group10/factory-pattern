package com.group10.scheduler.domain;
//singleton class 
public class ChiefEventCoordinator {
	long validOrganizationId;
	private static ChiefEventCoordinator instance = null;

//Private Constructor
	private ChiefEventCoordinator() {

	}
//Accessing the singleton object
	public static ChiefEventCoordinator getInstance() {
	    if (instance == null) {
	        instance = new ChiefEventCoordinator();
	    }

	    return instance;
	}
//Only the chief should create administrators 
	public Administrator generateAdministratorAccount(long adminId,String name,String email) {
		return new Administrator(adminId,name,email);
	}
}
