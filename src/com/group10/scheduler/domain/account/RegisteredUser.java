package com.group10.scheduler.domain.account;

public abstract class RegisteredUser {
	// Attributes are protected to protect data integrity; getter methods provide
	// controlled access.
	private String email;
	private String password;
	private String userName;
	protected String accountType;
	private String organizationId;

	// constructor
	public RegisteredUser(String email, String password, String accountType, String userName,  String organizationId) {

		this.email = email;
		this.password = password;
		this.accountType = accountType;
		this.userName = userName;
		this.organizationId = organizationId;
	}
	// getter methods
	
	public String getPassword() {
		return password; }

	public String getEmail() {
		return email;
	}

	public String getUserName() {
		return userName;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
}
