package vn.datnguy3n.marketplace.modules.order;

import vn.datnguy3n.marketplace.core.crud.BaseCRUDService;
import vn.datnguy3n.marketplace.modules.order.entity.Order;

import java.util.List;
import java.util.UUID;

public interface OrderService extends BaseCRUDService<Order> {

    List<Order> getByBuyer(UUID buyerId);

    List<Order> getBySeller(UUID sellerId);

    Order updateStatus(UUID id, String status, UUID requesterId);

    Order cancel(UUID id, UUID requesterId);
}
