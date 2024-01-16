package com.example.demo.service;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.BookSearchParametersDto;
import com.example.demo.dto.book.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto bookRequestDto);

    BookDto getBookById(Long id);

    List<BookDto> findAll(Pageable pageable);

    void deleteById(Long id);

    BookDto updateBookById(Long id, CreateBookRequestDto bookRequestDto);

    List<BookDto> search(BookSearchParametersDto searchParameters, Pageable pageable);
}
