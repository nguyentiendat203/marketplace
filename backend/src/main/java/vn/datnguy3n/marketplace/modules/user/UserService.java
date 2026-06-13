package vn.datnguy3n.marketplace.modules.user;

import vn.datnguy3n.marketplace.core.crud.BaseCRUDService;
import vn.datnguy3n.marketplace.modules.user.entity.User;

public interface UserService extends BaseCRUDService<User> {

    User findEntityByEmail(String email);

    boolean existsByEmail(String email);
}
