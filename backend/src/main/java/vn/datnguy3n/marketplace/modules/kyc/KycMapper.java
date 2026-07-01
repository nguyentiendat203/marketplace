package vn.datnguy3n.marketplace.modules.kyc;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import vn.datnguy3n.marketplace.core.crud.BaseMapper;
import vn.datnguy3n.marketplace.core.crud.BaseMapperConfig;
import vn.datnguy3n.marketplace.modules.kyc.dto.KycResponse;
import vn.datnguy3n.marketplace.modules.kyc.entity.KycRecord;

@Mapper(config = BaseMapperConfig.class)
public abstract class KycMapper implements BaseMapper<KycRecord, KycResponse> {

    @Override
    public abstract  KycResponse toDto(KycRecord entity);

    @Override
    public abstract KycRecord toEntity(KycResponse dto);

    @Override
    public abstract void updateEntityFromDto(KycResponse dto, @MappingTarget KycRecord entity);
}
