package com.Omnicanalidad.Omnicanalidad4J.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

@Service
public class DemoService {

    // El Retry maneja el Backoff Exponencial antes de que el error llegue al CB
    @Retry(name = "demoRetry")
    @CircuitBreaker(name = "demoCB")
    public String execute() {
        System.out.println(">>> [Servicio] Ejecutando intento...");
        throw new RuntimeException("Fallo de conexión simulado");
    }
}