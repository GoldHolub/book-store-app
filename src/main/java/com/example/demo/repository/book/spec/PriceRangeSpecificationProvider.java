package com.example.demo.repository.book.spec;

import com.example.demo.model.Book;
import com.example.demo.repository.SpecificationProviderForRange;
import java.math.BigDecimal;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PriceRangeSpecificationProvider
        implements SpecificationProviderForRange<Book, BigDecimal> {
    private static final String PRICE = "price";

    @Override
    public Specification<Book> getSpecification(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.between(root.get(PRICE), minPrice, maxPrice);
    }
}
