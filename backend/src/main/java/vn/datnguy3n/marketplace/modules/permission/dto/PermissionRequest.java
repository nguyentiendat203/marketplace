package vn.datnguy3n.marketplace.modules.permission.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionRequest {

    @NotBlank
    @Size(max = 100)
    private String pmsName;

    @NotBlank
    @Size(max = 255)
    private String pmsApiPath;

    @NotBlank
    @Pattern(regexp = "^(GET|POST|PUT|PATCH|DELETE)$", message = "pmsApiMethod phải là GET, POST, PUT, PATCH hoặc DELETE")
    private String pmsApiMethod;

    @Size(max = 100)
    private String pmsApiModule;
}
