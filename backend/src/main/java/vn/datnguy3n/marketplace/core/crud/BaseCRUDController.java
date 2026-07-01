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

import vn.datnguy3n.marketplace.common.ResultPaginationResponse;

public abstract class BaseCRUDController<T extends BaseEntity, D> {

    protected final BaseCRUDService<T, D> getService;

    public BaseCRUDController(BaseCRUDService<T, D> getService) {
        this.getService = getService;
    }

    @PostMapping
    public ResponseEntity<D> create(@RequestBody D dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(getService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<D> update(@PathVariable("id") UUID id, @RequestBody D dto) {
        return ResponseEntity.ok(getService.update(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<D> getById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(getService.getById(id));
    }

    @GetMapping
    public ResponseEntity<ResultPaginationResponse> getAll(@Filter Specification<T> spec, Pageable pageable) {
        return ResponseEntity.ok(getService.getAll(spec, pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        getService.delete(id);
        return ResponseEntity.ok(null);
    }
}
