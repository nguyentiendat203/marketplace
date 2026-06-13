package vn.datnguy3n.marketplace.core.crud;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

// Because Spring Data JPA doesn't allow us to inject a generic repository, we need to mark this interface with @NoRepositoryBean to prevent Spring from trying to create an instance of it.
@NoRepositoryBean
public interface BaseRepository<T> extends JpaRepository<T, UUID>, JpaSpecificationExecutor<T> {
}
