package com.example.demo.service.impl;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.BookSearchParametersDto;
import com.example.demo.dto.book.CreateBookRequestDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.model.Category;
import com.example.demo.repository.book.BookRepository;
import com.example.demo.repository.book.BookSpecificationBuilder;
import com.example.demo.repository.category.CategoryRepository;
import com.example.demo.service.BookService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;
    private final CategoryRepository categoryRepository;

    @Override
    public BookDto save(CreateBookRequestDto bookRequestDto) {
        Book bookModel = bookMapper.toModel(bookRequestDto);
        Set<Category> categories = setCategoriesByIds(bookRequestDto.getCategoryIds());
        bookModel.setCategories(categories);
        return bookMapper.toDto(bookRepository.save(bookModel));
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id: " + id));
        return bookMapper.toDto(book);
    }

    @Override
    @Transactional
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id: " + id));
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto updateBookById(Long id, CreateBookRequestDto bookRequestDto) {
        bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id: " + id));
        Book newBook = bookMapper.toModel(bookRequestDto);
        Set<Category> categories = setCategoriesByIds(bookRequestDto.getCategoryIds());
        newBook.setCategories(categories);
        newBook.setId(id);
        return bookMapper.toDto(bookRepository.save(newBook));
    }

    @Override
    @Transactional
    public List<BookDto> search(BookSearchParametersDto searchParameters, Pageable pageable) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(searchParameters);
        return bookRepository.findAll(bookSpecification, pageable)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDto> findAllByCategoryId(Long categoryId, Pageable pageable) {
        return bookRepository.findBooksByCategoryId(categoryId, pageable).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    private Set<Category> setCategoriesByIds(Set<Long> categoryIds) {
        return categoryIds.stream()
                .map(id -> categoryRepository.findById(id).orElseThrow(
                        () -> new EntityNotFoundException("Can't find category by id: " + id)))
                .collect(Collectors.toSet());
    }
}
