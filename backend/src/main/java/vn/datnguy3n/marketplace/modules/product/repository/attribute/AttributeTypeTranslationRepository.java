package vn.datnguy3n.marketplace.modules.product.repository.attribute;

import java.util.Optional;
import java.util.UUID;

import vn.datnguy3n.marketplace.core.crud.BaseRepository;
import vn.datnguy3n.marketplace.modules.product.entity.AttributeTypeTranslation;

public interface AttributeTypeTranslationRepository extends BaseRepository<AttributeTypeTranslation> {

    Optional<AttributeTypeTranslation> findByAttributeType_IdAndLanguageCode(UUID attributeTypeId, String languageCode);
}
