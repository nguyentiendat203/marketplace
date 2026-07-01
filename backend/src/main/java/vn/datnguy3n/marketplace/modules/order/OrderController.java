package vn.datnguy3n.marketplace.modules.order;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.datnguy3n.marketplace.core.crud.BaseCRUDController;
import vn.datnguy3n.marketplace.modules.order.dto.OrderResponse;
import vn.datnguy3n.marketplace.modules.order.entity.Order;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController extends BaseCRUDController<Order, OrderResponse> {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        super(orderService);
        this.orderService = orderService;
    }

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<OrderResponse>> getByBuyer(@PathVariable UUID buyerId) {
        return ResponseEntity.ok(orderService.getByBuyer(buyerId));
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<OrderResponse>> getBySeller(@PathVariable UUID sellerId) {
        return ResponseEntity.ok(orderService.getBySeller(sellerId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable UUID id,
            @RequestParam String status,
            @RequestParam UUID requesterId) {
        return ResponseEntity.ok(orderService.updateStatus(id, status, requesterId));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancel(
            @PathVariable UUID id,
            @RequestParam UUID requesterId) {
        return ResponseEntity.ok(orderService.cancel(id, requesterId));
    }
}
