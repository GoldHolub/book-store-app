package com.example.demo.mapper;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.CreateBookRequestDto;
import com.example.demo.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto bookRequestDto);
}
