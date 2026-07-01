package vn.datnguy3n.marketplace.modules.product.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import vn.datnguy3n.marketplace.core.crud.BaseMapper;
import vn.datnguy3n.marketplace.core.crud.BaseMapperConfig;
import vn.datnguy3n.marketplace.modules.product.dto.ProductResponse;
import vn.datnguy3n.marketplace.modules.product.entity.Product;

@Mapper(config = BaseMapperConfig.class)
public interface ProductMapper extends BaseMapper<Product, ProductResponse> {

    @Override
    ProductResponse toDto(Product entity);

    @Override
    Product toEntity(ProductResponse dto);

    @Override
    void updateEntityFromDto(ProductResponse dto, @MappingTarget Product entity);
}
