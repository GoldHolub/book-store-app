package com.example.demo.service;

import com.example.demo.dto.BookDto;
import com.example.demo.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto bookRequestDto);

    BookDto getBookById(Long id);

    List<BookDto> findAll();

    void deleteById(Long id);

    BookDto updateBookById(Long id, CreateBookRequestDto bookRequestDto);
}
