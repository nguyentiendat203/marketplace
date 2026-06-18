package vn.datnguy3n.marketplace.modules.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.datnguy3n.marketplace.core.crud.BaseCRUDServiceImpl;
import vn.datnguy3n.marketplace.modules.user.entity.User;

@Service
public class UserServiceImpl extends BaseCRUDServiceImpl<User> implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        super(userRepository);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void beforeCreate(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActivationKey(UUID.randomUUID().toString());
    }

    @Override
    public User findEntityByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public void updateRefreshToken(UUID userId, String refreshToken) {
        User user = getById(userId);
        user.setRefreshToken(refreshToken);
        repository.save(user);
    }

    @Override
    public Optional<User> findByRefreshToken(String refreshToken) {
        return userRepository.findByRefreshToken(refreshToken);
    }

    @Override
    public Optional<User> findByActivationKey(String key) {
        return userRepository.findByActivationKey(key);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
