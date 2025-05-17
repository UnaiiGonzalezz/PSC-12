package com.example.restapi.client;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.restapi.model.Book;

public class BookManager {

    private static final String BOOK_CONTROLLER_URL_TEMPLATE = "http://%s:%s/api/books";
    private final String BOOK_CONTROLLER_URL;
    private final RestTemplate restTemplate;

    public BookManager(String hostname, String port) {
        this.BOOK_CONTROLLER_URL = String.format(BOOK_CONTROLLER_URL_TEMPLATE, hostname, port);
        this.restTemplate = new RestTemplate();
    }

    public void registerBook(Book book) {
        ResponseEntity<Void> response = restTemplate.postForEntity(BOOK_CONTROLLER_URL, book, Void.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Book registered successfully.");
        } else {
            System.out.println("Failed to register book. Status code: " + response.getStatusCode());
        }
    }

    public List<Book> getAllBooks() {
        ResponseEntity<Book[]> response = restTemplate.getForEntity(BOOK_CONTROLLER_URL, Book[].class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return List.of(response.getBody());
        } else {
            System.out.println("Failed to retrieve books. Status code: " + response.getStatusCode());
            return List.of();
        }
    }

    public void deleteBook(String bookId) {
        try {
            restTemplate.delete(BOOK_CONTROLLER_URL + "/" + bookId);
            System.out.println("Book deleted successfully.");
        } catch (RestClientException e) {
            System.out.println("Failed to delete book. " + e.getMessage());
        }
    }
}
