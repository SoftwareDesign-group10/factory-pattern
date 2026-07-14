package com.group10.scheduler.domain.account;

public class Administrator {
    private String adminID;
    private String name;
    private String email;

    public Administrator(String adminID, String name, String email) {
        this.adminID = adminID;
        this.name = name;
        this.email = email;
    }

    public String getAdminID() { return adminID; }
    public String getName() { return name; }
    public String getEmail() { return email; }
}
