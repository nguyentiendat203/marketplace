package vn.datnguy3n.marketplace.modules.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import vn.datnguy3n.marketplace.core.crud.BaseMapper;
import vn.datnguy3n.marketplace.core.crud.BaseMapperConfig;
import vn.datnguy3n.marketplace.modules.role.RoleMapper;
import vn.datnguy3n.marketplace.modules.user.dto.UserResponse;
import vn.datnguy3n.marketplace.modules.user.entity.User;

@Mapper(config = BaseMapperConfig.class, uses = {RoleMapper.class})
public interface UserMapper extends BaseMapper<User, UserResponse> {

    @Override
    @Mapping(source = "activated", target = "active")
    UserResponse toDto(User entity);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "refreshToken", ignore = true)
    @Mapping(target = "activationKey", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(source = "active", target = "activated")
    User toEntity(UserResponse dto);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "refreshToken", ignore = true)
    @Mapping(target = "activationKey", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(source = "active", target = "activated")
    void updateEntityFromDto(UserResponse dto, @MappingTarget User entity);
}
