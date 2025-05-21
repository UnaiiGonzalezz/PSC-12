package com.example.restapi.performance;

import com.example.restapi.service.ClienteService;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.JUnitPerfTestRequirement;
import com.github.noconnor.junitperf.reporting.providers.CsvReportGenerator;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Tag("performance")
public class ClienteServicePerfTest {

    @Autowired
    private ClienteService clienteService;

    @Test
    @JUnitPerfTest(threads = 4, durationMs = 4000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(meanLatency = 200, maxLatency = 600, percentiles = "95:400")
    void testGetAllClientesPerformance() {
        clienteService.getAllClientes();
    }
}

