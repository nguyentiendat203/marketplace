package vn.datnguy3n.marketplace.modules.order;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.datnguy3n.marketplace.common.ApiResponse;
import vn.datnguy3n.marketplace.core.crud.BaseCRUDController;
import vn.datnguy3n.marketplace.modules.order.entity.Order;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController extends BaseCRUDController<Order> {

    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        super(orderService);
        this.orderService = orderService;
    }

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<ApiResponse<List<Order>>> getByBuyer(@PathVariable UUID buyerId) {
        return ResponseEntity.ok(ApiResponse.ok("Orders fetched", HttpStatus.OK.value(), orderService.getByBuyer(buyerId)));
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<ApiResponse<List<Order>>> getBySeller(@PathVariable UUID sellerId) {
        return ResponseEntity.ok(ApiResponse.ok("Orders fetched", HttpStatus.OK.value(), orderService.getBySeller(sellerId)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Order>> updateStatus(
            @PathVariable UUID id,
            @RequestParam String status,
            @RequestParam UUID requesterId) {
        return ResponseEntity.ok(ApiResponse.ok("Order status updated", HttpStatus.OK.value(), orderService.updateStatus(id, status, requesterId)));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<Order>> cancel(
            @PathVariable UUID id,
            @RequestParam UUID requesterId) {
        return ResponseEntity.ok(ApiResponse.ok("Order cancelled", HttpStatus.OK.value(), orderService.cancel(id, requesterId)));
    }
}
