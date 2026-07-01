package vn.datnguy3n.marketplace.modules.user;

import java.util.Optional;
import java.util.UUID;

import vn.datnguy3n.marketplace.core.crud.BaseCRUDService;
import vn.datnguy3n.marketplace.modules.user.dto.ChangePasswordRequest;
import vn.datnguy3n.marketplace.modules.user.dto.UserResponse;
import vn.datnguy3n.marketplace.modules.user.entity.User;

public interface UserService extends BaseCRUDService<User, UserResponse> {

    User findEntityByEmail(String email);

    boolean existsByEmail(String email);

    void updateRefreshToken(UUID userId, String refreshToken);

    Optional<User> findByRefreshToken(String refreshToken);

    Optional<User> findByActivationKey(String key);

    User saveUser(User user);

    String changePassword(ChangePasswordRequest request);
}
