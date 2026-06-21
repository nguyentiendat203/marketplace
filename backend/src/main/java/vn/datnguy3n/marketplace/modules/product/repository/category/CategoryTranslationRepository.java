package vn.datnguy3n.marketplace.modules.product.repository.category;

import java.util.Optional;
import java.util.UUID;

import vn.datnguy3n.marketplace.core.crud.BaseRepository;
import vn.datnguy3n.marketplace.modules.product.entity.CategoryTranslation;

public interface CategoryTranslationRepository extends BaseRepository<CategoryTranslation> {

    Optional<CategoryTranslation> findByCategory_IdAndLanguageCode(UUID categoryId, String languageCode);
}
