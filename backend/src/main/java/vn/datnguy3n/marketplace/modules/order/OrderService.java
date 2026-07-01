package vn.datnguy3n.marketplace.modules.order;

import vn.datnguy3n.marketplace.core.crud.BaseCRUDService;
import vn.datnguy3n.marketplace.modules.order.dto.OrderResponse;
import vn.datnguy3n.marketplace.modules.order.entity.Order;

import java.util.List;
import java.util.UUID;

public interface OrderService extends BaseCRUDService<Order, OrderResponse> {

    List<OrderResponse> getByBuyer(UUID buyerId);

    List<OrderResponse> getBySeller(UUID sellerId);

    OrderResponse updateStatus(UUID id, String status, UUID requesterId);

    OrderResponse cancel(UUID id, UUID requesterId);
}
