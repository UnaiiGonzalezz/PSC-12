package com.example.restapi.client;

import com.example.restapi.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class BookManagerConsoleUITest {

    private BookManager mockManager;

    @BeforeEach
    void setup() {
        mockManager = mock(BookManager.class);

        doNothing().when(mockManager).registerBook(any(Book.class));
        when(mockManager.getAllBooks()).thenReturn(List.of(new Book("Title", "Author", "123")));
        doNothing().when(mockManager).deleteBook(anyString());
    }

    @Test
    void testRun_fullFlow_allOptionsCovered() {
        String input =
                "1\n" +
                "Test Title\n" +
                "Test Author\n" +
                "123-456\n" +
                "2\n" +
                "3\n" +
                "1\n" +
                "2\n" +
                "5\n" +
                "texto\n" +
                "4\n";

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        Book mockBook = mock(Book.class);
        when(mockBook.getId()).thenReturn(99L);
        when(mockBook.getTitle()).thenReturn("Título");
        when(mockBook.getAuthor()).thenReturn("Autor");
        when(mockBook.getIsbn()).thenReturn("ABC-123");

        when(mockManager.getAllBooks()).thenReturn(List.of(mockBook));

        BookManagerConsoleUI ui = new BookManagerConsoleUI(mockManager);
        ui.run("localhost", "8080"); // ✅ CORRECTA FIRMA

        System.setOut(originalOut);

        verify(mockManager, atLeastOnce()).registerBook(any());
        verify(mockManager, atLeast(2)).getAllBooks();
        verify(mockManager).deleteBook(anyString());
    }

    @Test
    void testStart_argumentosInvalidos() {
        BookManagerConsoleUI ui = new BookManagerConsoleUI(mockManager);
        ui.start(new String[]{}); // ejecuta el path de "Usage:"
    }

    @Test
    void testRun_conEntradaNoNumerica_lanzaExcepcionYContinua() {
        String input = String.join("\n",
                "texto", // entrada inválida
                "4"      // salir
        ) + "\n";

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        BookManagerConsoleUI ui = new BookManagerConsoleUI(mockManager);
        assertDoesNotThrow(() -> ui.run("localhost", "8080")); // ✅ CORRECTA FIRMA
    }

    @Test
    void testMain_lanzaStartCorrectamente() {
        String[] args = {"localhost", "8080"};

        // Simula entrada para no quedarse esperando
        String input = "4\n"; // opción salir
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Captura la salida para forzar ejecución completa
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));

        assertDoesNotThrow(() -> BookManagerConsoleUI.main(args));

        System.setOut(originalOut);
    }

}
