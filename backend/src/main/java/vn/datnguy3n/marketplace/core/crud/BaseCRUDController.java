package vn.datnguy3n.marketplace.core.crud;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import vn.datnguy3n.marketplace.common.ApiResponse;
import vn.datnguy3n.marketplace.common.BaseEntity;

public abstract class BaseCRUDController<T extends BaseEntity> {

    protected final  BaseCRUDService<T> getService ;
    public BaseCRUDController(BaseCRUDService<T> getService) {
        this.getService = getService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<T>> create(@RequestBody T entity) {
        
        T created = getService.create(entity);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Created successfully", HttpStatus.CREATED.value(), created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<T>> update(@PathVariable UUID id, @RequestBody T entity) {
        T updated = getService.update(id, entity);
        return ResponseEntity.ok(ApiResponse.ok("Updated successfully", HttpStatus.OK.value(), updated));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<T>> getById(@PathVariable UUID id) {
        T found = getService.getById(id);
        return ResponseEntity.ok(ApiResponse.ok("Fetched successfully", HttpStatus.OK.value(), found));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<T>>> getAll(Pageable pageable) {
        Page<T> page = getService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.ok("Fetched successfully", HttpStatus.OK.value(), page));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        getService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Deleted successfully", HttpStatus.OK.value(), null));
    }
}
