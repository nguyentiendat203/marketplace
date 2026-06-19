package vn.datnguy3n.marketplace.modules.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import vn.datnguy3n.marketplace.core.crud.BaseEntity;
import vn.datnguy3n.marketplace.modules.role.entity.Role;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends BaseEntity {

    @Column(name = "usr_email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "usr_password", nullable = false)
    private String password;

    @Column(name = "usr_full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "usr_avatar_url")
    private String avatarUrl;

    @Column(name = "usr_phone", length = 20)
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usr_role_id")
    private Role role;

    @Column(name = "usr_is_activated", nullable = false)
    private boolean activated = false;

    @Column(name = "usr_is_kyc_verified", nullable = false)
    private boolean kycVerified = false;

    @Column(name = "usr_refresh_token", columnDefinition = "TEXT")
    private String refreshToken;

    @Column(name = "usr_activation_key", unique = true)
    private String activationKey;
}
