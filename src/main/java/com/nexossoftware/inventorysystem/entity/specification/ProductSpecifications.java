package com.nexossoftware.inventorysystem.entity.specification;

import com.nexossoftware.inventorysystem.entity.Product;
import com.nexossoftware.inventorysystem.entity.User;
import org.springframework.data.jpa.domain.Specification;

import java.time.temporal.ChronoUnit;
import java.util.Date;

public class ProductSpecifications {
    public static Specification<Product> nameContains(String name) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Product> userIs(User user) {
        return (root, query, cb) -> cb.equal(root.get("registeredUser"), user);
    }

    public static Specification<Product> dateIs(Date date) {
        Date from = Date.from(date.toInstant().truncatedTo(ChronoUnit.DAYS));
        Date to = Date.from(from.toInstant().plus(1, ChronoUnit.DAYS));
        return (root, query, cb) -> cb.between(root.get("entryDate"), from, to);
    }
}
