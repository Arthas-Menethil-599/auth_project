package org.itstep.auth_project.repositories;

import org.itstep.auth_project.entities.DbUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<DbUser, Long> {
    DbUser findUserByEmail(String email);
}
