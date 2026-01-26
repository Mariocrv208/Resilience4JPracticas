package com.Omnicanalidad.Omnicanalidad4J.service;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
public class CircuitBreakerLogger {

    private final DemoService demoService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final CircuitBreakerRegistry cbRegistry;
    private final RetryRegistry retryRegistry;
    private final String ACTUATOR_URL = "http://localhost:3000/actuator/circuitbreakers";
    private final Random random = new Random();
    private final List<String> logBuffer = new ArrayList<>();

    public CircuitBreakerLogger(DemoService demoService, CircuitBreakerRegistry cbRegistry, RetryRegistry retryRegistry) {
        this.demoService = demoService;
        this.cbRegistry = cbRegistry;
        this.retryRegistry = retryRegistry;
        setupEventListeners();
    }

    // Configura escuchadores para capturar eventos específicos de Resilience4j
    private void setupEventListeners() {
        // Eventos del Circuit Breaker (Cambios de estado)
        cbRegistry.circuitBreaker("demoCB").getEventPublisher()
                .onStateTransition(event -> {
                    addLogEntry("CAMBIO DE ESTADO", "De " + event.getStateTransition().getFromState() + " a " + event.getStateTransition().getToState());
                });

        // Eventos de Retry (Reintentos y Backoff)
        retryRegistry.retry("demoRetry").getEventPublisher()
                .onRetry(event -> {
                    addLogEntry("RETRY (Backoff Activo)", "Intento número: " + event.getNumberOfRetryAttempts());
                });
    }

    private void addLogEntry(String accion, String detalle) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        logBuffer.add(String.format("[%s] ACCIÓN: %-20s | %s", timestamp, accion, detalle));
    }

    public void runExperiment() {
        logBuffer.clear();
        long endTime = System.currentTimeMillis() + 30000; // 30 segundos
        System.out.println("Iniciando ráfaga con monitoreo de eventos...");

        while (System.currentTimeMillis() < endTime) {
            boolean shouldFail = random.nextInt(100) < 50;
            String resultDetail;

            try {
                if (shouldFail) {
                    demoService.execute();
                    resultDetail = "EJECUCIÓN EXITOSA (Tras recuperación)";
                    addLogEntry("EXITO", resultDetail);
                } else {
                    resultDetail = "EJECUCIÓN EXITOSA (Directa)";
                    addLogEntry("EXITO", resultDetail);
                }
            } catch (Exception e) {
                resultDetail = e.getMessage();
                addLogEntry("FALLO", resultDetail);
            }

            // Consultar Actuator para ver el búfer y estado actual
            captureActuatorSnapshot();

            try { Thread.sleep(200); } catch (InterruptedException ignored) {}
        }
        saveToFile();
    }

    private void captureActuatorSnapshot() {
        try {
            Map<String, Object> fullResponse = restTemplate.getForObject(ACTUATOR_URL, Map.class);
            Map<String, Object> demoCB = (Map<String, Object>) ((Map)fullResponse.get("circuitBreakers")).get("demoCB");

            String state = (String) demoCB.get("state");
            String buffered = demoCB.get("bufferedCalls").toString();

            // Si el estado es CLOSED (equivalente a UP/Sano), indicamos que se va al búfer
            if (state.equals("CLOSED")) {
                addLogEntry("BUFFERING", "Servicio UP - Petición añadida al búfer (Total: " + buffered + ")");
            }
        } catch (Exception ignored) {}
    }

    private void saveToFile() {
        try (FileWriter fw = new FileWriter("log_detallado_resilience.txt", false);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println("=================================================================================");
            pw.println("INFORME DETALLADO DE EVENTOS DE RESILIENCIA");
            pw.println("=================================================================================");
            logBuffer.forEach(pw::println);
            System.out.println("Log generado en 'log_detallado_resilience.txt'");
        } catch (Exception e) { e.printStackTrace(); }
    }
}