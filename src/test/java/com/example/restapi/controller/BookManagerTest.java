package com.example.restapi.controller;
import org.springframework.context.annotation.Import;
import com.example.restapi.testconfig.TestSecurityConfig;
import org.springframework.context.annotation.Import;
import com.example.restapi.client.BookManager;
import com.example.restapi.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class BookManagerTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BookManager bookManager;

    private final String hostname = "localhost";
    private final String port = "8080";
    private final String BASE_URL = "http://" + hostname + ":" + port + "/api/books";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookManager = new BookManager(hostname, port);
        // Override the default RestTemplate with mock
        java.lang.reflect.Field restTemplateField;
        try {
            restTemplateField = BookManager.class.getDeclaredField("restTemplate");
            restTemplateField.setAccessible(true);
            restTemplateField.set(bookManager, restTemplate);
        } catch (Exception e) {
            throw new RuntimeException("Error injecting mock RestTemplate", e);
        }
    }

    @Test
    void registerBook_success() {
        Book book = new Book("Title", "Author", "ISBN123");
        when(restTemplate.postForEntity(eq(BASE_URL), eq(book), eq(Void.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        bookManager.registerBook(book);

        verify(restTemplate).postForEntity(eq(BASE_URL), eq(book), eq(Void.class));
    }

    @Test
    void getAllBooks_success() {
        Book[] mockBooks = {
            new Book("Book A", "Author A", "ISBN001"),
            new Book("Book B", "Author B", "ISBN002")
        };

        when(restTemplate.getForEntity(BASE_URL, Book[].class))
                .thenReturn(new ResponseEntity<>(mockBooks, HttpStatus.OK));

        List<Book> result = bookManager.getAllBooks();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("Book A");
    }

    @Test
    void deleteBook_success() {
        String bookId = "123";

        doNothing().when(restTemplate).delete(BASE_URL + "/" + bookId);

        bookManager.deleteBook(bookId);

        verify(restTemplate).delete(BASE_URL + "/" + bookId);
    }

    @Test
    void deleteBook_errorHandled() {
        String bookId = "404";

        doThrow(new RestClientException("Not found"))
                .when(restTemplate).delete(BASE_URL + "/" + bookId);

        bookManager.deleteBook(bookId);

        verify(restTemplate).delete(BASE_URL + "/" + bookId);
    }
}
