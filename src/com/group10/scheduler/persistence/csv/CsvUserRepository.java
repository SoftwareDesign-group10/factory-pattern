package com.group10.scheduler.persistence.csv;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.group10.scheduler.domain.account.Faculty;
import com.group10.scheduler.domain.account.Partner;
import com.group10.scheduler.domain.account.RegisteredUser;
import com.group10.scheduler.domain.account.Staff;
import com.group10.scheduler.domain.account.Student;
import com.group10.scheduler.persistence.UserRepository;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvUserRepository implements UserRepository {

    private final String filePath;
    private static final String[] HEADERS = {"accountType", "email", "password", "userName", "organizationId"};

    public CsvUserRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<RegisteredUser> loadUsers() {
        List<RegisteredUser> users = new ArrayList<>();
        CsvReader reader = null;
        try {
            reader = new CsvReader(filePath);
            reader.readHeaders();
            while (reader.readRecord()) {
                String accountType = reader.get("accountType");
                String email = reader.get("email");
                String password = reader.get("password");
                String userName = reader.get("userName");
                long orgId = Long.parseLong(reader.get("organizationId"));

                RegisteredUser user;
                switch (accountType) {
                    case "STUDENT": user = new Student(email, password, accountType, userName); break;
                    case "FACULTY": user = new Faculty(email, password, accountType, userName); break;
                    case "STAFF": user = new Staff(email, password, accountType, userName); break;
                    case "PARTNER": user = new Partner(email, password, accountType, userName); break;
                    default: throw new IllegalArgumentException("Unknown accountType: " + accountType);
                }
                user.setOrganizationId(orgId);
                users.add(user);
            }
        } catch (IOException e) {
            System.out.println("No existing users.csv found, starting empty: " + e.getMessage());
        } finally {
            if (reader != null) reader.close();
        }
        return users;
    }

    @Override
    public void saveUsers(List<RegisteredUser> users) {
        CsvWriter writer = null;
        try {
            writer = new CsvWriter(new FileWriter(filePath, false), ',');
            for (String h : HEADERS) writer.write(h);
            writer.endRecord();

            for (RegisteredUser u : users) {
                writer.write(u.getAccountType());
                writer.write(u.getEmail());
                writer.write(u.getPassword());
                writer.write(u.getUserName());
                writer.write(String.valueOf(u.getOrganizationId()));
                writer.endRecord();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save users.csv", e);
        } finally {
            if (writer != null) writer.close();
        }
    }
}
