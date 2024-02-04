package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

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
import com.example.demo.service.impl.BookServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    public void save_ValidCreateBookRequestDto_ReturnsValidBookDto() {
        CreateBookRequestDto bookRequestDto = new CreateBookRequestDto();
        bookRequestDto.setAuthor("Max");
        bookRequestDto.setTitle("New adventure");
        bookRequestDto.setPrice(BigDecimal.valueOf(57.5));
        bookRequestDto.setCategoryIds(Set.of(1L));
        bookRequestDto.setIsbn("978-0-061-96436-7");

        Category category = new Category();
        category.setName("Adventure");
        category.setDescription("Adventure books");
        category.setId(1L);

        Book book = new Book();
        book.setAuthor("Max");
        book.setTitle("New adventure");
        book.setPrice(BigDecimal.valueOf(57.5));
        book.setCategories(Set.of(category));
        book.setIsbn("978-0-061-96436-7");
        book.setId(1L);

        BookDto bookDto = new BookDto();
        bookDto.setAuthor("Max");
        bookDto.setTitle("New adventure");
        bookDto.setPrice(BigDecimal.valueOf(57.5));
        bookDto.setCategoryIds(Set.of(1L));
        bookDto.setIsbn("978-0-061-96436-7");
        bookDto.setId(1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(bookMapper.toModel(bookRequestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto savedBookDto = bookService.save(bookRequestDto);

        assertEquals(bookDto, savedBookDto);
    }

    @Test
    public void getBookById_validId_validBook() {
        Long validBookId = 1L;
        Book book = new Book();
        book.setId(validBookId);
        book.setTitle("Valid Book");

        BookDto bookDto = new BookDto();
        bookDto.setId(validBookId);
        bookDto.setTitle("Valid Book");

        when(bookRepository.findById(validBookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto retrievedBookDto = bookService.getBookById(validBookId);

        assertNotNull(retrievedBookDto);
        assertEquals(book.getId(), retrievedBookDto.getId());
    }

    @Test
    public void getBookById_invalidId_notOk() {
        Long invalidBookId = 99L;

        when(bookRepository.findById(invalidBookId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.getBookById(invalidBookId));
    }

    @Test
    public void updateBookById_ValidData_UpdatedBook() {
        Long validBookId = 1L;
        Book existingBook = new Book();
        existingBook.setId(validBookId);
        existingBook.setTitle("Existing Book");

        CreateBookRequestDto updatedBookRequestDto = new CreateBookRequestDto();
        updatedBookRequestDto.setAuthor("Updated Author");
        updatedBookRequestDto.setTitle("Updated Title");

        Book updatedBook = new Book();
        updatedBook.setId(validBookId);
        updatedBook.setAuthor("Updated Author");
        updatedBook.setTitle("Updated Title");

        BookDto updatedBookDto = new BookDto();
        updatedBookDto.setId(validBookId);
        updatedBookDto.setAuthor("Updated Author");
        updatedBookDto.setTitle("Updated Title");

        when(bookRepository.findById(validBookId)).thenReturn(Optional.of(existingBook));
        when(bookMapper.toModel(updatedBookRequestDto)).thenReturn(updatedBook);
        when(bookRepository.save(updatedBook)).thenReturn(updatedBook);
        when(bookMapper.toDto(updatedBook)).thenReturn(updatedBookDto);

        BookDto resultBookDto = bookService.updateBookById(validBookId, updatedBookRequestDto);

        assertNotNull(resultBookDto);
        assertEquals(validBookId, resultBookDto.getId());
        assertEquals("Updated Author", resultBookDto.getAuthor());
        assertEquals("Updated Title", resultBookDto.getTitle());
    }

    @Test
    public void updateBookById_InvalidId_notOk() {
        Long invalidBookId = 99L;
        CreateBookRequestDto updatedBookRequestDto = new CreateBookRequestDto();

        when(bookRepository.findById(invalidBookId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> bookService.updateBookById(invalidBookId, updatedBookRequestDto));
    }

    @Test
    public void search_ZeroConditions_ListWithTwoBooks() {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book 1");

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Book 2");

        List<Book> books = List.of(book1, book2);
        Page<Book> bookPage = new PageImpl<>(books);
        Specification<Book> spec = Specification.where(null);
        BookSearchParametersDto searchParameters = new BookSearchParametersDto();
        Pageable pageable = Pageable.unpaged();

        when(bookSpecificationBuilder.build(searchParameters)).thenReturn(spec);
        when(bookRepository.findAll(spec, pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book1)).thenReturn(new BookDto());
        when(bookMapper.toDto(book2)).thenReturn(new BookDto());

        List<BookDto> result = bookService.search(searchParameters, pageable);

        assertEquals(2, result.size());
    }
}
