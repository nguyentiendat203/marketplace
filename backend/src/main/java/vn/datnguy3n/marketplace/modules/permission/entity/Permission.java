package vn.datnguy3n.marketplace.modules.permission.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import vn.datnguy3n.marketplace.core.crud.BaseEntity;
import vn.datnguy3n.marketplace.modules.role.entity.Role;

@Entity
@Table(name = "permissions")
@Getter
@Setter
public class Permission extends BaseEntity {

    @Column(name = "pms_name", nullable = false, unique = true, length = 100)
    private String pmsName;

    @Column(name = "pms_api_path", nullable = false, length = 255)
    private String pmsApiPath;

    @Column(name = "pms_api_method", nullable = false, length = 10)
    private String pmsApiMethod;

    @Column(name = "pms_api_module", length = 100)
    private String pmsApiModule;

    @JsonIgnore
    @ManyToMany(mappedBy = "permissions")
    private List<Role> roles;
}
