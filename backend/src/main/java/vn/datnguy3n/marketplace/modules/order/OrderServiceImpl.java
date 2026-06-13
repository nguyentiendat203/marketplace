package vn.datnguy3n.marketplace.modules.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.datnguy3n.marketplace.modules.order.dto.OrderRequest;
import vn.datnguy3n.marketplace.modules.order.dto.OrderResponse;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public OrderResponse create(OrderRequest request) {
        // TODO: check user block via UserService, resolve product price, persist Order + OrderAddress
        return null;
    }

    @Override
    public OrderResponse getById(UUID id) {
        // TODO: throw NotFoundException if absent, map to DTO
        return null;
    }

    @Override
    public List<OrderResponse> getByBuyer(UUID buyerId) {
        // TODO: map entities to DTOs
        return List.of();
    }

    @Override
    public List<OrderResponse> getBySeller(UUID sellerId) {
        // TODO: map entities to DTOs
        return List.of();
    }

    @Override
    public OrderResponse updateStatus(UUID id, String status, UUID requesterId) {
        // TODO: ownership check, validate status transition, persist, sync with PaymentService
        return null;
    }

    @Override
    public OrderResponse cancel(UUID id, UUID requesterId) {
        // TODO: ownership check, set status=CANCELLED, trigger Stripe refund via PaymentService
        return null;
    }
}
