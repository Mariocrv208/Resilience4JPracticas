package com.Omnicanalidad.Omnicanalidad4J.controller;

import com.Omnicanalidad.Omnicanalidad4J.Data.DatosRequestLogin;
import com.Omnicanalidad.Omnicanalidad4J.Data.DatosRequestPrelogin;
import com.Omnicanalidad.Omnicanalidad4J.service.CircuitBreakerLogger;
import com.Omnicanalidad.Omnicanalidad4J.service.EjercicioServicios;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class DemoController {

    private final CircuitBreakerLogger logger;

    private final EjercicioServicios prelogin;

    private final EjercicioServicios login;

    private final EjercicioServicios data;

    public DemoController(CircuitBreakerLogger logger, EjercicioServicios prelogin, EjercicioServicios login, EjercicioServicios data) {
        this.logger = logger;
        this.prelogin = prelogin;
        this.login = login;
        this.data = data;
    }


    @GetMapping("/demo")
    public String start() {
        logger.runExperiment();
        return "Experimento completado. Archivo generado.";
    }

    @PostMapping("/prelogin")
    public ResponseEntity<String> preLogin(@RequestBody DatosRequestPrelogin datosRequestPrelogin){
        return ResponseEntity.ok(
          prelogin.preLogin(datosRequestPrelogin)
        );
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody DatosRequestLogin datosRequestLogin, HttpSession session){
        return ResponseEntity.ok(
                login.Login(datosRequestLogin, session)
        );
    }

    @GetMapping("/data")
    public ResponseEntity<String> data(HttpSession session) {
        String respuesta =data.data(session);

        return ResponseEntity.ok(respuesta);
    }
}