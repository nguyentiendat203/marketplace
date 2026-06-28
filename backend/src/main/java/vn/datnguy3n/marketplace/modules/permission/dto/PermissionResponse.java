package vn.datnguy3n.marketplace.modules.permission.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionResponse {

    private UUID id;
    private String pmsName;
    private String pmsApiPath;
    private String pmsApiMethod;
    private String pmsApiModule;
}
