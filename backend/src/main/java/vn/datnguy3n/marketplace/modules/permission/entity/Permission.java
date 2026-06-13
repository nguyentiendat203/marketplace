package vn.datnguy3n.marketplace.modules.permission.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import vn.datnguy3n.marketplace.common.BaseEntity;

@Entity
@Table(name = "permissions")
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
public class Permission extends BaseEntity {

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "resource", nullable = false, length = 100)
    private String resource;

    @Column(name = "action", nullable = false, length = 50)
    private String action;

    @Column(name = "description", length = 255)
    private String description;
}
