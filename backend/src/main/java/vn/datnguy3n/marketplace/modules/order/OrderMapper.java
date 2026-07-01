package vn.datnguy3n.marketplace.modules.order;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import vn.datnguy3n.marketplace.core.crud.BaseMapper;
import vn.datnguy3n.marketplace.core.crud.BaseMapperConfig;
import vn.datnguy3n.marketplace.modules.order.dto.OrderResponse;
import vn.datnguy3n.marketplace.modules.order.entity.Order;
import vn.datnguy3n.marketplace.modules.order.entity.OrderAddress;

@Mapper(config = BaseMapperConfig.class)
public interface OrderMapper extends BaseMapper<Order, OrderResponse> {

    @Override
    OrderResponse toDto(Order entity);

    OrderResponse.ShippingAddressInfo toShippingAddressInfo(OrderAddress address);

    @Override
    Order toEntity(OrderResponse dto);

    @Override
    void updateEntityFromDto(OrderResponse dto, @MappingTarget Order entity);

}
