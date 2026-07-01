package vn.datnguy3n.marketplace.core.crud;

import org.mapstruct.MappingTarget;

public interface BaseMapper<T, D> {

    D toDto(T entity);

    T toEntity(D dto);

    void updateEntityFromDto(D dto, @MappingTarget T entity);
}
