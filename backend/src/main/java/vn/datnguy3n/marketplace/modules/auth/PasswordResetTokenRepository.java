package vn.datnguy3n.marketplace.modules.auth;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.datnguy3n.marketplace.core.crud.BaseRepository;
import vn.datnguy3n.marketplace.modules.auth.entity.PasswordResetToken;

public interface PasswordResetTokenRepository extends BaseRepository<PasswordResetToken> {

    Optional<PasswordResetToken> findByToken(String token);

    @Modifying
    @Query(value = "DELETE FROM password_reset_tokens WHERE user_id = :userId", nativeQuery = true)
    void deleteAllByUserId(@Param("userId") UUID userId);

    @Modifying
    @Query(value = "DELETE FROM password_reset_tokens WHERE id = :id", nativeQuery = true)
    void deleteByIdPhysically(@Param("id") UUID id);
}
