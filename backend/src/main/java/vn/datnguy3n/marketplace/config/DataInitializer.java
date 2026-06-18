package vn.datnguy3n.marketplace.config;


import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import vn.datnguy3n.marketplace.modules.role.RoleRepository;
import vn.datnguy3n.marketplace.modules.role.entity.Role;

@Service
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!roleRepository.existsByName("ROLE_USER")) {
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            userRole.setDescription("Default role for registered users");
            roleRepository.save(userRole);
        }

        if (!roleRepository.existsByName("ROLE_ADMIN")) {
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            adminRole.setDescription("System administrator");
            roleRepository.save(adminRole);
        }
    }
}
