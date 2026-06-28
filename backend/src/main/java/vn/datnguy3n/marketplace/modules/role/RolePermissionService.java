package vn.datnguy3n.marketplace.modules.role;

import java.util.Collection;

public interface RolePermissionService {

    boolean hasPermissionForRequest(Collection<String> roleAuthorities, String apiPath, String httpMethod);
}
