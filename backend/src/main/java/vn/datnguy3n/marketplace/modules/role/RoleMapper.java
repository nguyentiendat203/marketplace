package vn.datnguy3n.marketplace.modules.role;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import vn.datnguy3n.marketplace.core.crud.BaseMapper;
import vn.datnguy3n.marketplace.core.crud.BaseMapperConfig;
import vn.datnguy3n.marketplace.modules.permission.PermissionMapper;
import vn.datnguy3n.marketplace.modules.role.dto.RoleResponse;
import vn.datnguy3n.marketplace.modules.role.entity.Role;

@Mapper(config = BaseMapperConfig.class, uses = {PermissionMapper.class})
public interface RoleMapper extends BaseMapper<Role, RoleResponse> {

    @Override
    RoleResponse toDto(Role entity);

    @Override
    Role toEntity(RoleResponse dto);

    @Override
    void updateEntityFromDto(RoleResponse dto, @MappingTarget Role entity);
}
