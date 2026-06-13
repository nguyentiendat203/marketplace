package vn.datnguy3n.marketplace.modules.user;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.datnguy3n.marketplace.common.ApiResponse;
import vn.datnguy3n.marketplace.modules.user.dto.UserRequest;
import vn.datnguy3n.marketplace.modules.user.dto.UserResponse;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok("Users fetched", 200, userService.getAll()));
        // return ResponseEntity.ok(ApiResponse.ok("Users fetched", userService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok("User fetched", 200, userService.getById(id)));
    }
    // @GetMapping("/{id}")
    // public ResponseEntity<ApiResponse<UserResponse>> getById(@PathVariable UUID id) {
    //     return ResponseEntity.ok(ApiResponse.ok("User fetched", 200, userService.getById(id)));
    // }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("User updated", 200, userService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        userService.softDelete(id);
        return ResponseEntity.ok(ApiResponse.ok("User deleted", 200, null));
    }
}
