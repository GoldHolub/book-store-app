package com.example.demo.repository;

import org.springframework.data.jpa.domain.Specification;

public interface SpecificationProviderForRange<T, N extends Number> {
    Specification<T> getSpecification(N fromVal, N toVal);
}
