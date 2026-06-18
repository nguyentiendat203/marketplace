package vn.datnguy3n.marketplace.core.crud;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import vn.datnguy3n.marketplace.common.ApplicationContextProvider;

@MappedSuperclass
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    private Instant createdAt;

    private String createdBy;

    private Instant updatedAt;

    private String updatedBy;

    private Instant deletedAt;

    private String deletedBy;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdAt = Instant.now();
        this.createdBy = ApplicationContextProvider.getCurrentUserLogin().orElse("");
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedAt = Instant.now();
        this.updatedBy = ApplicationContextProvider.getCurrentUserLogin().orElse("");
    }

    // @PreRemove
    // public void handleBeforeDelete() {
    //     this.deletedAt = Instant.now();
    //     this.deletedBy = ApplicationContextProvider.getCurrentUserLogin().orElse("");
    // }

}
