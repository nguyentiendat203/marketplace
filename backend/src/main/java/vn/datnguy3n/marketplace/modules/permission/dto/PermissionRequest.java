package vn.datnguy3n.marketplace.modules.permission.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionRequest {

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 100)
    private String resource;

    @NotBlank
    @Size(max = 50)
    private String action;

    @Size(max = 255)
    private String description;
}
