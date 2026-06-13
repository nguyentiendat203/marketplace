package vn.datnguy3n.marketplace.modules.user;

import java.time.Instant;

import org.springframework.stereotype.Service;

import vn.datnguy3n.marketplace.core.crud.BaseCRUDServiceImpl;
import vn.datnguy3n.marketplace.modules.user.entity.User;

@Service
public class UserServiceImpl extends BaseCRUDServiceImpl<User> implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }

    @Override
    protected void beforeCreate(User user) {
        
        // Lấy password thô từ JSON, mã hóa nó rồi nạp vào trường passwordHash để lưu xuống DB
        // Sau này bạn thay chữ "MOCK_HASH_" bằng BCryptPasswordEncoder là xong
        user.setPassword("MOCK_HASH_" + user.getPassword());
        user.setCreatedAt(Instant.now());
    }

    @Override
    public User findEntityByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
