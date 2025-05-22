package com.example.restapi.controller;

import com.example.restapi.model.Book;
import com.example.restapi.security.JwtUtil;
import com.example.restapi.service.BookService;
import com.example.restapi.testconfig.TestSecurityConfig;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestSecurityConfig.class)
@Tag("controller")
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void testGetAllBooks() throws Exception {
        when(bookService.getAllBooks()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/books"))
               .andExpect(status().isOk())
               .andExpect(content().json("[]"));
    }

    @Test
    void testGetBookById_found() throws Exception {
        Book book = new Book("Title", "Author", "123");
        book.setId(1L);
        when(bookService.getBookById(1L)).thenReturn(Optional.of(book));

        mockMvc.perform(get("/api/books/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.title", is("Title")))
               .andExpect(jsonPath("$.author", is("Author")))
               .andExpect(jsonPath("$.isbn", is("123")));
    }

    @Test
    void testGetBookById_notFound() throws Exception {
        when(bookService.getBookById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/books/99"))
               .andExpect(status().isNotFound());
    }

    @Test
    void testCreateBook_success() throws Exception {
        Book book = new Book("Nuevo", "Autor", "ABC");
        book.setId(2L);
        when(bookService.createBook(any())).thenReturn(book);

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Nuevo\",\"author\":\"Autor\",\"isbn\":\"ABC\"}"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.title", is("Nuevo")));
    }

    @Test
    void testCreateBook_failure() throws Exception {
        when(bookService.createBook(any())).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Error\",\"author\":\"Fail\",\"isbn\":\"000\"}"))
               .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateBook_success() throws Exception {
        Book updated = new Book("Actualizado", "Autor", "XYZ");
        updated.setId(3L);
        when(bookService.updateBook(eq(3L), any())).thenReturn(updated);

        mockMvc.perform(put("/api/books/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Actualizado\",\"author\":\"Autor\",\"isbn\":\"XYZ\"}"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.title", is("Actualizado")));
    }

    @Test
    void testUpdateBook_failure() throws Exception {
        when(bookService.updateBook(eq(4L), any())).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(put("/api/books/4")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Error\",\"author\":\"Fail\",\"isbn\":\"000\"}"))
               .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteBook_found() throws Exception {
        when(bookService.getBookById(5L)).thenReturn(Optional.of(new Book("T", "A", "I")));

        mockMvc.perform(delete("/api/books/5"))
               .andExpect(status().isNoContent());

        verify(bookService).deleteBook(5L);
    }

    @Test
    void testDeleteBook_notFound() throws Exception {
        when(bookService.getBookById(6L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/books/6"))
               .andExpect(status().isNotFound());

        verify(bookService, never()).deleteBook(6L);
        
    }
}
