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
    @JoinColumn(name = "kyc_user_id", nullable = false)
    private User user;

    @Column(name = "kyc_document_type", nullable = false, length = 50)
    private String documentType;

    @Column(name = "kyc_front_image_url", nullable = false)
    private String frontImageUrl;

    @Column(name = "kyc_back_image_url")
    private String backImageUrl;

    @Column(name = "kyc_selfie_url")
    private String selfieUrl;

    @Column(name = "kyc_status", nullable = false, length = 20)
    private KycStatus status = KycStatus.PENDING;

    @Column(name = "kyc_reviewed_by", length = 36)
    private String reviewedBy;

    @Column(name = "kyc_review_note", length = 500)
    private String reviewNote;
}
