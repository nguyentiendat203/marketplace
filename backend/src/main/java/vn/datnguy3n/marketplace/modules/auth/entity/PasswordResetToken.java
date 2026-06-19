package vn.datnguy3n.marketplace.modules.auth.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import vn.datnguy3n.marketplace.core.crud.BaseEntity;
import vn.datnguy3n.marketplace.modules.user.entity.User;

@Entity
@Table(name = "password_reset_tokens")
@Getter
@Setter
public class PasswordResetToken extends BaseEntity {

    @Column(name = "prt_token", nullable = false)
    private String token;

    @Column(name = "prt_expiry_date", nullable = false)
    private Instant expiryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prt_user_id", nullable = false)
    private User user;
}
