package vn.datnguy3n.marketplace.modules.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.datnguy3n.marketplace.core.crud.BaseCRUDController;
import vn.datnguy3n.marketplace.modules.user.dto.ChangePasswordRequest;
import vn.datnguy3n.marketplace.modules.user.dto.UserResponse;
import vn.datnguy3n.marketplace.modules.user.entity.User;

@RestController
@RequestMapping("/api/v1/users")
public class UserController extends BaseCRUDController<User, UserResponse> {

    private final UserService userService;

    public UserController(UserService userService) {
        super(userService);
        this.userService = userService;
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        return ResponseEntity.ok(userService.changePassword(request));
    }
}
