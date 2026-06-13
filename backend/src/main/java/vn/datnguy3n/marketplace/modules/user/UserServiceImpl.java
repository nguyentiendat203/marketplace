package vn.datnguy3n.marketplace.modules.user;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.datnguy3n.marketplace.modules.user.dto.UserRequest;
import vn.datnguy3n.marketplace.modules.user.dto.UserResponse;
import vn.datnguy3n.marketplace.modules.user.entity.User;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public String   getById(UUID id) {
        // TODO: throw NotFoundException if not found, map to DTO
        return "Hello world";
    }
    // @Override
    // public UserResponse getById(UUID id) {
    //     // TODO: throw NotFoundException if not found, map to DTO
    //     return null;
    // }

    @Override
    public List<UserResponse> getAll() {
        // TODO: map entities to DTOs
        return List.of();
    }

    @Override
    public UserResponse update(UUID id, UserRequest request) {
        // TODO: fetch, apply patch, persist, return DTO
        return null;
    }

    @Override
    public void softDelete(UUID id) {
        // TODO: set deletedAt / deletedBy, persist
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
