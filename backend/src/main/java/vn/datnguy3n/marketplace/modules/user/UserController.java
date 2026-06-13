package vn.datnguy3n.marketplace.modules.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.datnguy3n.marketplace.core.crud.BaseCRUDController;
import vn.datnguy3n.marketplace.modules.user.entity.User;

@RestController
@RequestMapping("/api/v1/users")
public class UserController extends BaseCRUDController<User> {

    public UserController(UserService userService) {
        super(userService);
    }

}
