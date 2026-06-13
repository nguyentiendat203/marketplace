package vn.datnguy3n.marketplace.modules.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {

    @NotBlank
    @Size(max = 150)
    private String fullName;

    @Size(max = 20)
    private String phone;

    private String avatarUrl;
}
