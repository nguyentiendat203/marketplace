package vn.datnguy3n.marketplace.modules.permission.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PermissionResponse {

    private UUID id;
    private String name;
    private String resource;
    private String action;
    private String description;
}
