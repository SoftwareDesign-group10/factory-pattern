package com.group10.scheduler.domain.account;

/**
 * Base class from Farid's factory-pattern repo (github.com/SoftwareDesign-group10/factory-pattern),
 * integrated as-is except for the additions marked below. See CHANGES.md.
 */
public abstract class RegisteredUser {
	// Attributes are protected to protect data integrity; getter methods provide controlled access.
	private String email;
	private String password;
	private String userName;
	protected String accountType;
	protected double hourlyRate;
	private long organizationId;

	//constructor
	 public RegisteredUser(
	            String email,
	            String password,
	            String accountType,
	            String userName) {

	        this.email = email;
	        this.password = password;
	        this.userName = userName;
	        this.accountType = accountType;
	    }
	//getter methods
	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}
	public String getUserName() {
		return userName;
	}

	//will be implemented inside each of each subclass
	protected abstract void setHourlyRate();

	// --- ADDED for CSV persistence + booking integration (not in Farid's original) ---
	// accountType and hourlyRate already existed as protected fields with no public
	// accessor; CsvUserRepository and BookingManager need to read them from outside
	// the class hierarchy, so these getters were added. organizationId was declared
	// but never settable anywhere in the original -- added a setter/getter so the
	// factory can populate it and CSV can persist/reload it (Req1's student ID / org ID).
	public String getAccountType() { return accountType; }
	public double getHourlyRate() { return hourlyRate; }
	public long getOrganizationId() { return organizationId; }
	public void setOrganizationId(long organizationId) { this.organizationId = organizationId; }
}
