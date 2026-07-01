package vn.datnguy3n.marketplace.modules.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.datnguy3n.marketplace.common.ApplicationContextProvider;
import vn.datnguy3n.marketplace.core.crud.BaseMapper;
import vn.datnguy3n.marketplace.core.crud.BaseCRUDServiceImpl;
import vn.datnguy3n.marketplace.core.exception.BusinessException;
import vn.datnguy3n.marketplace.modules.user.dto.ChangePasswordRequest;
import vn.datnguy3n.marketplace.modules.user.dto.UserResponse;
import vn.datnguy3n.marketplace.modules.user.entity.User;

@Service
public class UserServiceImpl extends BaseCRUDServiceImpl<User, UserResponse> implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        super(userRepository);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    protected BaseMapper<User, UserResponse> getMapper() {
        return userMapper;
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
        User user = fetchEntityById(userId);
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

    @Override
    @Transactional
    public String changePassword(ChangePasswordRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException("Mật khẩu mới và xác nhận mật khẩu không trùng khớp", HttpStatus.BAD_REQUEST);
        }

        String currentEmail = ApplicationContextProvider.getCurrentUserLogin()
                .orElseThrow(() -> new BusinessException("Không tìm thấy thông tin người dùng hiện tại", HttpStatus.UNAUTHORIZED));

        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new BusinessException("Người dùng không tồn tại", HttpStatus.NOT_FOUND));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BusinessException("Mật khẩu cũ không chính xác", HttpStatus.BAD_REQUEST);
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return "Đổi mật khẩu thành công.";
    }
}
