package vn.datnguy3n.marketplace.modules.product.specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import vn.datnguy3n.marketplace.modules.product.entity.Product;
import vn.datnguy3n.marketplace.modules.product.entity.ProductAttribute;
import vn.datnguy3n.marketplace.modules.product.entity.ProductStatus;

public final class ProductSpecification {

    private ProductSpecification() {}

    public static Specification<Product> withFilters(
            UUID categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            List<UUID> attributeOptionIds) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("status"), ProductStatus.AVAILABLE));

            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }

            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            }

            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            if (attributeOptionIds != null && !attributeOptionIds.isEmpty()) {
                Join<Product, ProductAttribute> attrJoin = root.join("attributes", JoinType.INNER);
                predicates.add(attrJoin.get("attributeOption").get("id").in(attributeOptionIds));
                if (query != null) {
                    query.distinct(true);
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
