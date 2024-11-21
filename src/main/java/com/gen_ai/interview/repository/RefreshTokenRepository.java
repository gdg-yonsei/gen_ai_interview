package com.gen_ai.interview.repository;

import com.gen_ai.interview.model.RefreshToken;
import com.gen_ai.interview.model.User;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String token);

    Optional<RefreshToken> findByUser(User user);
}
