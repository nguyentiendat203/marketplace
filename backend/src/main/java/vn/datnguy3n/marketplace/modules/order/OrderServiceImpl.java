package vn.datnguy3n.marketplace.modules.order;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.datnguy3n.marketplace.core.crud.BaseMapper;
import vn.datnguy3n.marketplace.core.crud.BaseCRUDServiceImpl;
import vn.datnguy3n.marketplace.modules.order.dto.OrderResponse;
import vn.datnguy3n.marketplace.modules.order.entity.Order;

@Service
public class OrderServiceImpl extends BaseCRUDServiceImpl<Order, OrderResponse> implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper) {
        super(orderRepository);
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    protected BaseMapper<Order, OrderResponse> getMapper() {
        return orderMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderResponse> getByBuyer(UUID buyerId) {
        return orderRepository.findByBuyer_Id(buyerId).stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderResponse> getBySeller(UUID sellerId) {
        return orderRepository.findBySeller_Id(sellerId).stream().map(this::toDto).toList();
    }

    @Transactional
    @Override
    public OrderResponse updateStatus(UUID id, String status, UUID requesterId) {
        // TODO: ownership check, validate status transition, persist, sync with PaymentService
        return null;
    }

    @Transactional
    @Override
    public OrderResponse cancel(UUID id, UUID requesterId) {
        // TODO: ownership check, set status=CANCELLED, trigger Stripe refund via PaymentService
        return null;
    }
}
