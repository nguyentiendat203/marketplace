package vn.datnguy3n.marketplace.modules.role;

import org.springframework.stereotype.Service;
import vn.datnguy3n.marketplace.core.crud.BaseCRUDServiceImpl;
import vn.datnguy3n.marketplace.modules.role.entity.Role;

@Service
public class RoleServiceImpl extends BaseCRUDServiceImpl<Role> implements RoleService {

    public RoleServiceImpl(RoleRepository repository) {
        super(repository);
    }
}
