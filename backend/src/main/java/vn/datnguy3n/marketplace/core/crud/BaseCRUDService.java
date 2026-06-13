package vn.datnguy3n.marketplace.core.crud;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.datnguy3n.marketplace.common.BaseEntity;

public interface BaseCRUDService<T extends BaseEntity> {

    T create(T entity);

    T update(UUID id, T entity);

    T getById(UUID id);

    Page<T> getAll(Pageable pageable);

    void delete(UUID id);
}
