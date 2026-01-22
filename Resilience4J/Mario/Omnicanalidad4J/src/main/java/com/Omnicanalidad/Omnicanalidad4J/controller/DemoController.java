package com.Omnicanalidad.Omnicanalidad4J.controller;

import com.Omnicanalidad.Omnicanalidad4J.service.CircuitBreakerLogger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    private final CircuitBreakerLogger logger;

    public DemoController(CircuitBreakerLogger logger) {
        this.logger = logger;
    }

    @GetMapping("/demo")
    public String start() {
        logger.runExperiment();
        return "Experimento completado. Archivo generado.";
    }
}