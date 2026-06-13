package vn.datnguy3n.marketplace.modules.order;

import vn.datnguy3n.marketplace.modules.order.dto.OrderRequest;
import vn.datnguy3n.marketplace.modules.order.dto.OrderResponse;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderResponse create(OrderRequest request);

    OrderResponse getById(UUID id);

    List<OrderResponse> getByBuyer(UUID buyerId);

    List<OrderResponse> getBySeller(UUID sellerId);

    OrderResponse updateStatus(UUID id, String status, UUID requesterId);

    OrderResponse cancel(UUID id, UUID requesterId);
}
