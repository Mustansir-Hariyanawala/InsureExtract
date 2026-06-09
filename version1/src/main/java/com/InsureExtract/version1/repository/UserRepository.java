package com.InsureExtract.version1.repository;

import com.InsureExtract.version1.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    /**
     * Finds a user by their exact username.
     */
    Optional<UserEntity> findByUsername(String username);

    /**
     * Checks if a username already exists in the database.
     */
    boolean existsByUsername(String username);

    /**
     * Checks if an email already exists in the database.
     */
    boolean existsByEmail(String email);
}