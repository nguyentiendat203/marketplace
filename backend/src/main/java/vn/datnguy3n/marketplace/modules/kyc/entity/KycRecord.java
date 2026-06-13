package vn.datnguy3n.marketplace.modules.kyc.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import vn.datnguy3n.marketplace.common.BaseEntity;

import java.util.UUID;

@Entity
@Table(name = "kyc_records")
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
public class KycRecord extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "document_type", nullable = false, length = 50)
    private String documentType;

    @Column(name = "front_image_url", nullable = false)
    private String frontImageUrl;

    @Column(name = "back_image_url")
    private String backImageUrl;

    @Column(name = "selfie_url")
    private String selfieUrl;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "PENDING";

    @Column(name = "reviewed_by", length = 36)
    private String reviewedBy;

    @Column(name = "review_note", length = 500)
    private String reviewNote;
}
