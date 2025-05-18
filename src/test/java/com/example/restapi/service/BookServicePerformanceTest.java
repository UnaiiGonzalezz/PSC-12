package com.example.restapi.service;

import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.JUnitPerfTestRequirement;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Tag("service")
public class BookServicePerformanceTest {

    @Autowired
    private BookService bookService;

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 2000)
    @JUnitPerfTestRequirement(
        maxLatency = 500,
        meanLatency = 200,
        executionsPerSec = 5,
        allowedErrorPercentage = 0.1f
    )
    public void testGetAllBooksPerformance() {
        bookService.getAllBooks();
    }
}
