package vn.datnguy3n.marketplace.modules.order;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.datnguy3n.marketplace.modules.order.entity.Order;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findByBuyerId(UUID buyerId);

    List<Order> findBySellerId(UUID sellerId);

    List<Order> findByStatus(String status);
}
