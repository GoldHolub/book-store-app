package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.demo.model.Book;
import com.example.demo.repository.book.BookRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "classpath:database/books/add-books-and-category-to-db.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:database/books/delete-books-and-category-from-db.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    void findBooksByCategoryId_NonExistingId_ReturnEmptyList() {
        List<Book> books = bookRepository.findBooksByCategoryId(45L, null);
        assertEquals(0, books.size());
    }

    @Test
    void findBooksByCategoryId_NegativeValueOrZeroId_ReturnEmptyList() {
        List<Book> books = bookRepository.findBooksByCategoryId(-1L, null);
        assertEquals(0, books.size());
    }

    @Test
    void findBooksByCategoryId_CorrectId_ReturnList() {
        List<Book> books = bookRepository.findBooksByCategoryId(1L, null);
        assertEquals(2, books.size());
    }
}
