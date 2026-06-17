package vn.datnguy3n.marketplace.modules.user;

import java.util.Optional;

import vn.datnguy3n.marketplace.core.crud.BaseRepository;
import vn.datnguy3n.marketplace.modules.user.entity.User;

public interface UserRepository extends BaseRepository<User> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByRefreshToken(String refreshToken);
}
