package com.example.restapi.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookTest {

    @Test
    void testConstructorYGetters() {
        Book book = new Book("Clean Code", "Robert C. Martin", "978-0132350884");
        book.setId(1L);

        assertThat(book.getId()).isEqualTo(1L);
        assertThat(book.getTitle()).isEqualTo("Clean Code");
        assertThat(book.getAuthor()).isEqualTo("Robert C. Martin");
        assertThat(book.getIsbn()).isEqualTo("978-0132350884");
    }

    @Test
    void testSettersIndependientes() {
        Book book = new Book();
        book.setId(10L);
        book.setTitle("Refactoring");
        book.setAuthor("Martin Fowler");

        assertThat(book.getId()).isEqualTo(10L);
        assertThat(book.getTitle()).isEqualTo("Refactoring");
        assertThat(book.getAuthor()).isEqualTo("Martin Fowler");
    }
}
