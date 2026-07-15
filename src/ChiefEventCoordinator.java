//Singleton class
public class ChiefEventCoordinator {
	private long validOrganizationId;
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

//Setters and Getters
	public long getValidOrganizationId() {
		return validOrganizationId;
	}

	public void setValidOrganizationId(long validOrganizationId) {
		this.validOrganizationId = validOrganizationId;
	}
	public Administrator generateAdministratorAccount(long adminId,String name,String email) {
		return new Administrator( adminId, name, email);
	}
}
