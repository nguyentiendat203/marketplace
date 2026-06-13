package vn.datnguy3n.marketplace.modules.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import vn.datnguy3n.marketplace.common.BaseEntity;

import java.util.UUID;

@Entity
@Table(
    name = "user_blocks",
    uniqueConstraints = @UniqueConstraint(columnNames = {"blocker_id", "blocked_id"})
)
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
public class UserBlock extends BaseEntity {

    @Column(name = "blocker_id", nullable = false)
    private UUID blockerId;

    @Column(name = "blocked_id", nullable = false)
    private UUID blockedId;
}
