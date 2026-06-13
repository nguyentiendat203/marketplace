package vn.datnguy3n.marketplace.core.crud;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import vn.datnguy3n.marketplace.common.BaseEntity;
import vn.datnguy3n.marketplace.common.ResultPaginationResponse;

public interface BaseCRUDService<T extends BaseEntity> {

    T create(T entity);

    T update(UUID id, T entity);

    T getById(UUID id);

    ResultPaginationResponse getAll(Specification<T> spec, Pageable pageable);

    void delete(UUID id);
}
