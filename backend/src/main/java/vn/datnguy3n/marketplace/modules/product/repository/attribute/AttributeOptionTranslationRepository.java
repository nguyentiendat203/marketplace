package vn.datnguy3n.marketplace.modules.product.repository.attribute;

import java.util.Optional;
import java.util.UUID;

import vn.datnguy3n.marketplace.core.crud.BaseRepository;
import vn.datnguy3n.marketplace.modules.product.entity.AttributeOptionTranslation;

public interface AttributeOptionTranslationRepository extends BaseRepository<AttributeOptionTranslation> {

    Optional<AttributeOptionTranslation> findByAttributeOption_IdAndLanguageCode(UUID attributeOptionId, String languageCode);
}
