package vn.datnguy3n.marketplace.modules.user;

import java.util.List;
import java.util.UUID;

import vn.datnguy3n.marketplace.modules.user.dto.UserRequest;
import vn.datnguy3n.marketplace.modules.user.dto.UserResponse;
import vn.datnguy3n.marketplace.modules.user.entity.User;

public interface UserService {

    String getById(UUID id);
    // UserResponse getById(UUID id);

    List<UserResponse> getAll();

    UserResponse update(UUID id, UserRequest request);

    void softDelete(UUID id);

    User findEntityByEmail(String email);

    boolean existsByEmail(String email);
}
