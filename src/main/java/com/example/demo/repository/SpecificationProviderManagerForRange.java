package com.example.demo.repository;

public interface SpecificationProviderManagerForRange<T, N extends Number> {
    SpecificationProviderForRange<T, N> getSpecificationProvider(String key);
}
