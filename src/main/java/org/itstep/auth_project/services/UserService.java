package org.itstep.auth_project.services;

import org.itstep.auth_project.entities.DbUser;
import org.itstep.auth_project.entities.Role;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    DbUser getUser(String email);

    DbUser registerUser(DbUser user);

    DbUser updateUser(DbUser user);

    void deleteUser(Long id);

    List<DbUser> getUsers();

    List<DbUser> getUsersPaged(Integer currentPage, Integer length, Role role);
}
