package vn.datnguy3n.marketplace.modules.product.repository.brand;

import java.util.Optional;
import java.util.UUID;

import vn.datnguy3n.marketplace.core.crud.BaseRepository;
import vn.datnguy3n.marketplace.modules.product.entity.BrandTranslation;

public interface BrandTranslationRepository extends BaseRepository<BrandTranslation> {

    Optional<BrandTranslation> findByBrand_IdAndLanguageCode(UUID brandId, String languageCode);
}
