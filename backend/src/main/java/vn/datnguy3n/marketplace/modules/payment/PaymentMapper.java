package vn.datnguy3n.marketplace.modules.payment;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import vn.datnguy3n.marketplace.core.crud.BaseMapper;
import vn.datnguy3n.marketplace.core.crud.BaseMapperConfig;
import vn.datnguy3n.marketplace.modules.payment.dto.PaymentResponse;
import vn.datnguy3n.marketplace.modules.payment.entity.Payment;

@Mapper(config = BaseMapperConfig.class)
public interface PaymentMapper extends BaseMapper<Payment, PaymentResponse> {

    @Override
    PaymentResponse toDto(Payment entity);

    @Override
    Payment toEntity(PaymentResponse dto);

    @Override
    void updateEntityFromDto(PaymentResponse dto, @MappingTarget Payment entity);
}
