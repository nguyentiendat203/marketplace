package vn.datnguy3n.marketplace.modules.product.repository.product;

import java.util.Optional;
import java.util.UUID;

import vn.datnguy3n.marketplace.core.crud.BaseRepository;
import vn.datnguy3n.marketplace.modules.product.entity.ProductTranslation;

public interface ProductTranslationRepository extends BaseRepository<ProductTranslation> {

    Optional<ProductTranslation> findByProduct_IdAndLanguageCode(UUID productId, String languageCode);
}
