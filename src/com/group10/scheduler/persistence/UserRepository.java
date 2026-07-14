package com.group10.scheduler.persistence;

import com.group10.scheduler.domain.account.RegisteredUser;
import java.util.List;

public interface UserRepository {
    List<RegisteredUser> loadUsers();
    void saveUsers(List<RegisteredUser> users);
}
