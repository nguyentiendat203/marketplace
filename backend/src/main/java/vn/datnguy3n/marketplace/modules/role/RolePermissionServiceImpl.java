package vn.datnguy3n.marketplace.modules.role;

import java.util.Collection;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.AntPathMatcher;

import vn.datnguy3n.marketplace.modules.permission.entity.Permission;
import vn.datnguy3n.marketplace.modules.role.entity.Role;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private final RoleRepository roleRepository;

    public RolePermissionServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasPermissionForRequest(Collection<String> roleAuthorities, String apiPath, String httpMethod) {
       // 1: Duyệt qua từng Role mà User đang có trong Token (Ví dụ: [ROLE_USER, ROLE_MANAGER])
        for (String authority : roleAuthorities) {

            // 2: Gọi xuống DB để tìm Role theo tên, ĐỒNG THỜI kéo luôn danh sách Permissions của Role đó
            Role role = roleRepository.findByNameWithPermissions(authority).orElse(null);
            
            // 3: Nếu dưới DB không có Role này, hoặc Role này chưa được gán bất kỳ quyền nào 
            // -> Bỏ qua, đi kiểm tra Role tiếp theo của User.
            if (role == null || role.getPermissions() == null) {
                continue;
            }

            // 4: Role hợp lệ, bắt đầu duyệt qua từng Permission mà Role này đang nắm giữ
            for (Permission permission : role.getPermissions()) {

                // 5: Kiểm tra xem URL User đang gọi có khớp với URL khai báo trong Permission không?
                boolean pathMatches = antPathMatcher.match(permission.getPmsApiPath(), apiPath);
                
                // 6: Kiểm tra xem Method User đang gọi (GET, POST...) có khớp với Method trong Permission không?
                // (Dùng equalsIgnoreCase để lỡ tay lưu dưới DB là "post" hay "POST" thì đều nhận diện được)
                boolean methodMatches = permission.getPmsApiMethod().equalsIgnoreCase(httpMethod);
               
                // 7: Nếu ApiPath và Method đều khớp 100% 
                // -> User có quyền! Lập tức trả về true và kết thúc hàm.
                if (pathMatches && methodMatches) {
                    return true;
                }
            }
        }

        // 8: Đã duyệt hết tất cả các Permission của User nhưng không có cái nào khớp 
        // -> Từ chối truy cập (Access Denied).
        return false;
    }
}
