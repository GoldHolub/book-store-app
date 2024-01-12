package com.example.demo.repository.book;

import com.example.demo.model.Book;
import com.example.demo.repository.SpecificationProviderForRange;
import com.example.demo.repository.SpecificationProviderManagerForRange;
import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManagerForRange implements
        SpecificationProviderManagerForRange<Book, BigDecimal> {
    private final List<SpecificationProviderForRange<Book, BigDecimal>> bookSpecificationProviders;

    @Override
    public SpecificationProviderForRange<Book, BigDecimal> getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "Can't find correct specification provider by key: " + key));
    }
}
