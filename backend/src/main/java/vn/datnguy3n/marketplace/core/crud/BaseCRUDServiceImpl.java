package vn.datnguy3n.marketplace.core.crud;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import vn.datnguy3n.marketplace.common.BaseEntity;

public abstract class BaseCRUDServiceImpl<T extends BaseEntity> implements BaseCRUDService<T> {

    protected final JpaRepository<T, UUID> repository;
    protected BaseCRUDServiceImpl(JpaRepository<T, UUID> repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public T create(T entity) {
        beforeCreate(entity);
        T saved = repository.save(entity);
        afterCreate(saved);
        return saved;
    }

    @Transactional
    @Override
    public T update(UUID id, T entity) {
        getById(id);
        entity.setId(id);
        beforeUpdate(entity);
        T updated = repository.save(entity);
        afterUpdate(updated);
        return updated;
    }

    @Transactional(readOnly = true)
    @Override
    public T getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entity not found with id: " + id));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<T> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        getById(id);
        repository.deleteById(id);
    }

    protected void beforeCreate(T entity) {}

    protected void afterCreate(T entity) {}

    protected void beforeUpdate(T entity) {}

    protected void afterUpdate(T entity) {}
}
