package vn.datnguy3n.marketplace.modules.order;

import vn.datnguy3n.marketplace.core.crud.BaseRepository;
import vn.datnguy3n.marketplace.modules.order.entity.Order;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends BaseRepository<Order> {

    List<Order> findByBuyer_Id(UUID buyerId);

    List<Order> findBySeller_Id(UUID sellerId);

    List<Order> findByStatus(String status);
}
