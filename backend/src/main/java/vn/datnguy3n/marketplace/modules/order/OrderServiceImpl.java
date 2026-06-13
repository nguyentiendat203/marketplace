package vn.datnguy3n.marketplace.modules.order;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.datnguy3n.marketplace.core.crud.BaseCRUDServiceImpl;
import vn.datnguy3n.marketplace.modules.order.entity.Order;

import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl extends BaseCRUDServiceImpl<Order> implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        super(orderRepository);
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Order> getByBuyer(UUID buyerId) {
        return orderRepository.findByBuyer_Id(buyerId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Order> getBySeller(UUID sellerId) {
        return orderRepository.findBySeller_Id(sellerId);
    }

    @Transactional
    @Override
    public Order updateStatus(UUID id, String status, UUID requesterId) {
        // TODO: ownership check, validate status transition, persist, sync with PaymentService
        return null;
    }

    @Transactional
    @Override
    public Order cancel(UUID id, UUID requesterId) {
        // TODO: ownership check, set status=CANCELLED, trigger Stripe refund via PaymentService
        return null;
    }
}
