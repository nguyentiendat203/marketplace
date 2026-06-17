package vn.datnguy3n.marketplace.modules.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import vn.datnguy3n.marketplace.common.BaseEntity;

@Entity
@Table(
    name = "user_blocks",
    uniqueConstraints = @UniqueConstraint(columnNames = {"blocker_id", "blocked_id"})
)
@Getter
@Setter
public class UserBlock extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocker_id", nullable = false)
    private User blocker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocked_id", nullable = false)
    private User blocked;
}
