package com.group10.scheduler.domain.room;
public class ChiefEventCoordinator {
	long validOrganizationId;
	private static ChiefEventCoordinator instance = null;

//Constructor
	private ChiefEventCoordinator() {

	}

	public static ChiefEventCoordinator getInstance() {
	    if (instance == null) {
	        instance = new ChiefEventCoordinator();
	    }

	    return instance;
	}
}
