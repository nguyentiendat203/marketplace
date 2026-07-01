package vn.datnguy3n.marketplace.modules.permission;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import vn.datnguy3n.marketplace.core.crud.BaseMapper;
import vn.datnguy3n.marketplace.core.crud.BaseMapperConfig;
import vn.datnguy3n.marketplace.modules.permission.dto.PermissionResponse;
import vn.datnguy3n.marketplace.modules.permission.entity.Permission;

@Mapper(config = BaseMapperConfig.class)
public interface PermissionMapper extends BaseMapper<Permission, PermissionResponse> {

    @Override
    PermissionResponse toDto(Permission entity);

    @Override
    Permission toEntity(PermissionResponse dto);

    @Override
    void updateEntityFromDto(PermissionResponse dto, @MappingTarget Permission entity);
}
