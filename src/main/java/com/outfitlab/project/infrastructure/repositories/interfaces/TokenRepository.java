package com.outfitlab.project.infrastructure.repositories.interfaces;

import com.outfitlab.project.infrastructure.config.security.jwt.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query(
            value = """
                SELECT t FROM Token t WHERE t.user.id = :userId 
                    AND (t.expired = false AND t.revoked = false)
            """
    )
    List<Token> allValidTokensByUser(@Param("userId") Long id);

    Optional<Token> findByToken(String token);
}
