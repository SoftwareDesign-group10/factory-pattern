package com.group10.scheduler.domain.account;

import com.group10.scheduler.persistence.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class AccountManagement {
    private final UserRepository userRepository;
    private final RegisteredUserFactory factory;
    private List<RegisteredUser> users;

    public AccountManagement(UserRepository userRepository, RegisteredUserFactory factory) {
        this.userRepository = userRepository;
        this.factory = factory;
        this.users = new ArrayList<>(userRepository.loadUsers());
    }

    public boolean verifyUniqueEmail(String email) {
        return users.stream().noneMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }

    public boolean validateStrongPassword(String password) {
        // Req1: uppercase, lowercase, numbers, and symbols
        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSymbol = password.chars().anyMatch(c -> !Character.isLetterOrDigit(c));
        return password.length() >= 8 && hasUpper && hasLower && hasDigit && hasSymbol;
    }

    public RegisteredUser createAccount(String email, String password, String userName, String accountType, long id) {
        if (!verifyUniqueEmail(email)) {
            throw new IllegalArgumentException("Email already in use: " + email);
        }
        if (!validateStrongPassword(password)) {
            throw new IllegalArgumentException("Password does not meet strength requirements.");
        }
        RegisteredUser user = factory.createUser(email, password, accountType, userName, id);
        users.add(user);
        userRepository.saveUsers(users);
        return user;
    }

    public RegisteredUser findByEmail(String email) {
        return users.stream().filter(u -> u.getEmail().equalsIgnoreCase(email)).findFirst().orElse(null);
    }

    public List<RegisteredUser> getAllUsers() {
        return users;
    }
}
