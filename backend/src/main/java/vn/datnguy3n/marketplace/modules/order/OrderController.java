package vn.datnguy3n.marketplace.modules.order;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.datnguy3n.marketplace.common.ApiResponse;
import vn.datnguy3n.marketplace.modules.order.dto.OrderRequest;
import vn.datnguy3n.marketplace.modules.order.dto.OrderResponse;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> create(@Valid @RequestBody OrderRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Order created", 200, orderService.create(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok("Order fetched", 200, orderService.getById(id)));
    }

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getByBuyer(@PathVariable UUID buyerId) {
        return ResponseEntity.ok(ApiResponse.ok("Orders fetched", 200, orderService.getByBuyer(buyerId)));
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getBySeller(@PathVariable UUID sellerId) {
        return ResponseEntity.ok(ApiResponse.ok("Orders fetched", 200, orderService.getBySeller(sellerId)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateStatus(
            @PathVariable UUID id,
            @RequestParam String status,
            @RequestParam UUID requesterId) {
        return ResponseEntity.ok(ApiResponse.ok("Order status updated", 200, orderService.updateStatus(id, status, requesterId)));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<OrderResponse>> cancel(
            @PathVariable UUID id,
            @RequestParam UUID requesterId) {
        return ResponseEntity.ok(ApiResponse.ok("Order cancelled", 200, orderService.cancel(id, requesterId)));
    }
}
