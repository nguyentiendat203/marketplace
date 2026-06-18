package vn.datnguy3n.marketplace.modules.kyc.entity;

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
@Table(name = "kyc_records")
@Getter
@Setter
public class KycRecord extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 50)
    private String documentType;

    @Column(nullable = false)
    private String frontImageUrl;

    private String backImageUrl;

    private String selfieUrl;

    @Column(nullable = false, length = 20)
    private KycStatus status = KycStatus.PENDING;

    @Column(length = 36)
    private String reviewedBy;

    @Column(length = 500)
    private String reviewNote;
}
