package vn.datnguy3n.marketplace.core.crud;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import vn.datnguy3n.marketplace.common.ApplicationContextProvider;
import vn.datnguy3n.marketplace.common.ResultPaginationResponse;
import vn.datnguy3n.marketplace.core.exception.BusinessException;

@Service
public abstract class BaseCRUDServiceImpl<T extends BaseEntity> implements BaseCRUDService<T> {

    protected final BaseRepository<T> repository;

    protected BaseCRUDServiceImpl(BaseRepository<T> repository) {
        this.repository = repository;
    }

    @Override
    public T create(T entity) {
        beforeCreate(entity);
        T saved = repository.save(entity);
        afterCreate(saved);
        return saved;
    }

    @Override
    public T update(UUID id, T entity) {
        getById(id);
        entity.setId(id);
        beforeUpdate(entity);
        T updated = repository.save(entity);
        afterUpdate(updated);
        return updated;
    }

    @Override
    public T getById(UUID id) {
        Optional<T> entity = repository.findById(id);
        if (entity.isPresent()) {
            return entity.get();
        }
        throw new BusinessException("Entity not found with id: " + id, HttpStatus.NOT_FOUND);
    }

    @Override
    public ResultPaginationResponse getAll(Specification<T> spec, Pageable pageable) {
        Page<T> page = repository.findAll(spec, pageable);

        ResultPaginationResponse.Meta meta = new ResultPaginationResponse.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(page.getTotalPages());
        meta.setTotal(page.getTotalElements());

        ResultPaginationResponse result = new ResultPaginationResponse();
        result.setMeta(meta);
        result.setResult(page.getContent());
        return result;
    }

    @Override
    public void delete(UUID id) {
        T entity = getById(id); 
        
        entity.setDeletedAt(Instant.now());
        entity.setDeletedBy(ApplicationContextProvider.getCurrentUserLogin().orElse(""));
        
        repository.save(entity);
    }

    protected void beforeCreate(T entity) {}

    protected void afterCreate(T entity) {}

    protected void beforeUpdate(T entity) {}

    protected void afterUpdate(T entity) {}
}
