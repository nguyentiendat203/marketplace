package vn.datnguy3n.marketplace.modules.permission;

import java.util.Optional;

import vn.datnguy3n.marketplace.core.crud.BaseRepository;
import vn.datnguy3n.marketplace.modules.permission.entity.Permission;

public interface PermissionRepository extends BaseRepository<Permission> {

    Optional<Permission> findByPmsApiPathAndPmsApiMethod(String pmsApiPath, String pmsApiMethod);
}
