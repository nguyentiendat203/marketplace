package vn.datnguy3n.marketplace.modules.role;

import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.datnguy3n.marketplace.core.crud.BaseCRUDServiceImpl;
import vn.datnguy3n.marketplace.modules.role.entity.Role;

@Service
public class RoleServiceImpl extends BaseCRUDServiceImpl<Role> implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        super(roleRepository);
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }
}
