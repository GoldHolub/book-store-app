package com.example.demo.repository.book;

import com.example.demo.dto.book.BookSearchParametersDto;
import com.example.demo.model.Book;
import com.example.demo.repository.SpecificationBuilder;
import com.example.demo.repository.SpecificationProviderManager;
import com.example.demo.repository.SpecificationProviderManagerForRange;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private static final String AUTHOR = "author";
    private static final String TITLE = "title";
    private static final String PRICE = "price";
    private final SpecificationProviderManager<Book> specificationProviderManager;
    private final SpecificationProviderManagerForRange<Book, BigDecimal>
                                    specificationProviderManagerForRange;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParameters) {
        Specification<Book> spec = Specification.where(null);
        if (searchParameters.authors() != null && searchParameters.authors().length > 0) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider(AUTHOR).getSpecification(searchParameters.authors()));
        }
        if (searchParameters.titles() != null && searchParameters.titles().length > 0) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider(TITLE).getSpecification(searchParameters.titles()));
        }
        if (searchParameters.maxPrice() != null
                && searchParameters.maxPrice().compareTo(BigDecimal.ZERO) > 0
                && searchParameters.minPrice() != null
                && searchParameters.minPrice().compareTo(BigDecimal.ZERO) > 0
                && searchParameters.maxPrice().compareTo(searchParameters.minPrice()) > 0) {
            spec = spec.and(specificationProviderManagerForRange.getSpecificationProvider(PRICE)
                    .getSpecification(searchParameters.minPrice(), searchParameters.maxPrice()));
        }
        return spec;
    }
}
