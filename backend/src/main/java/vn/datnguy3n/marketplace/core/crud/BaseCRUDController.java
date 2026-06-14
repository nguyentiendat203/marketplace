package vn.datnguy3n.marketplace.core.crud;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.turkraft.springfilter.boot.Filter;

import vn.datnguy3n.marketplace.common.BaseEntity;
import vn.datnguy3n.marketplace.common.ResultPaginationResponse;

public abstract class BaseCRUDController<T extends BaseEntity> {

    protected final BaseCRUDService<T> getService;

    public BaseCRUDController(BaseCRUDService<T> getService) {
        this.getService = getService;
    }

    @PostMapping
    public ResponseEntity<T> create(@RequestBody T entity) {
        T created = getService.create(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<T> update(@PathVariable UUID id, @RequestBody T entity) {
        T updated = getService.update(id, entity);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<T> getById(@PathVariable UUID id) {
        T found = getService.getById(id);
        return ResponseEntity.ok(found);
    }

    @GetMapping
    public ResponseEntity<ResultPaginationResponse> getAll(@Filter Specification<T> spec,
            Pageable pageable) {
        ResultPaginationResponse result = getService.getAll(spec, pageable);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        getService.delete(id);
        return ResponseEntity.ok(null);
    }
}
