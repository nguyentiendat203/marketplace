package vn.datnguy3n.marketplace.core.crud;

import java.time.Instant;
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
public abstract class BaseCRUDServiceImpl<T extends BaseEntity, D> implements BaseCRUDService<T, D> {

    protected final BaseRepository<T> repository;

    protected BaseCRUDServiceImpl(BaseRepository<T> repository) {
        this.repository = repository;
    }

    protected abstract BaseMapper<T, D> getMapper();

    protected D toDto(T entity) {
        return getMapper().toDto(entity);
    }

    protected T fetchEntityById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new BusinessException("Entity not found with id: " + id, HttpStatus.NOT_FOUND));
    }

    @Override
    public D create(D dto) {
        T entity = getMapper().toEntity(dto);
        beforeCreate(entity);
        T saved = repository.save(entity);
        afterCreate(saved);
        return toDto(saved);
    }

    @Override
    public D update(UUID id, D dto) {
        T existing = fetchEntityById(id);
        getMapper().updateEntityFromDto(dto, existing);
        beforeUpdate(existing);
        T updated = repository.save(existing);
        afterUpdate(updated);
        return toDto(updated);
    }

    @Override
    public D getById(UUID id) {
        return toDto(fetchEntityById(id));
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
        result.setResult(page.getContent().stream().map(this::toDto).toList());
        return result;
    }

    @Override
    public void delete(UUID id) {
        T entity = fetchEntityById(id);
        entity.setDeletedAt(Instant.now());
        entity.setDeletedBy(ApplicationContextProvider.getCurrentUserLogin().orElse(""));
        repository.save(entity);
    }

    protected void beforeCreate(T entity) {}

    protected void afterCreate(T entity) {}

    protected void beforeUpdate(T entity) {}

    protected void afterUpdate(T entity) {}
}
