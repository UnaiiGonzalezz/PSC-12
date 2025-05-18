package com.example.restapi.service;

import com.example.restapi.model.Book;
import com.example.restapi.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBooks() {
        List<Book> books = Arrays.asList(new Book(), new Book());
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookService.getAllBooks();

        assertEquals(2, result.size());
        verify(bookRepository).findAll();
    }

    @Test
    void testGetBookById() {
        Book book = new Book();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Optional<Book> result = bookService.getBookById(1L);

        assertTrue(result.isPresent());
        verify(bookRepository).findById(1L);
    }

    @Test
    void testCreateBook() {
        Book book = new Book();
        when(bookRepository.save(book)).thenReturn(book);

        Book result = bookService.createBook(book);

        assertEquals(book, result);
        verify(bookRepository).save(book);
    }

    @Test
    void testUpdateBookSuccess() {
        Book existing = new Book();
        Book update = new Book();
        update.setTitle("New Title");
        update.setAuthor("New Author");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(bookRepository.save(existing)).thenReturn(existing);

        Book result = bookService.updateBook(1L, update);

        assertEquals("New Title", existing.getTitle());
        assertEquals("New Author", existing.getAuthor());
    }

    @Test
    void testUpdateBookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookService.updateBook(1L, new Book()));

        assertEquals("Book not found", exception.getMessage());
    }

    @Test
    void testDeleteBookSuccess() {
        when(bookRepository.existsById(1L)).thenReturn(true);

        bookService.deleteBook(1L);

        verify(bookRepository).deleteById(1L);
    }

    @Test
    void testDeleteBookNotFound() {
        when(bookRepository.existsById(1L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookService.deleteBook(1L));

        assertEquals("Book not found with id: 1", exception.getMessage());
    }
}
