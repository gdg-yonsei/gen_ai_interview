package com.gen_ai.interview.repository;

import com.gen_ai.interview.model.User;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    boolean existsByName(String name);

    boolean existsByEmail(String email);

    Optional<User> findByName(String name);

    Optional<User> findByEmail(String email);
}
