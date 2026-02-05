package com.Omnicanalidad.Omnicanalidad4J.service;

import com.Omnicanalidad.Omnicanalidad4J.Data.DatosRequestPrelogin;
import com.Omnicanalidad.Omnicanalidad4J.Data.DatosRequestLogin;
import jakarta.servlet.http.HttpSession;
import org.apache.coyote.Response;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.client.RestTemplate;

import javax.print.attribute.standard.Media;
import java.util.List;

@RestController
public class EjercicioServicios {


    private final RestTemplate restTemplate;


    public EjercicioServicios(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }


    public String preLogin(DatosRequestPrelogin datosRequestPrelogin){

        // consumo de WCR/prelogin
        String url = "http://10.160.209.146:9084/CWCREST/services/resources/cobis/cwc/credentials/public/validate";

        HttpEntity<DatosRequestPrelogin> entity = new HttpEntity<DatosRequestPrelogin>(datosRequestPrelogin);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );
        return response.getBody();
    }

    public String Login(DatosRequestLogin datosRequestLogin, HttpSession session){

        // consumo de WCR/login
        String url = "http://10.160.209.146:9084/CWCREST/services/resources/cobis/cwc/authentication/public/login";

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<DatosRequestLogin> entity = new HttpEntity<>(datosRequestLogin, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );

        response.getHeaders().forEach((k,v) ->System.out.println("HEADER "+ k+ " = "+ v));

        String authorizationHeader = response.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if(authorizationHeader == null){
            authorizationHeader = response.getHeaders().getFirst("X-Authorization");
        }
        if(authorizationHeader == null){
            authorizationHeader = response.getHeaders().getFirst("X-Auth-Token");
        }
        if(authorizationHeader == null){
            throw new RuntimeException("El backend no expone el bearer token");
        }

        session.setAttribute("BEARER_TOKEN", authorizationHeader);

        return authorizationHeader;
    }

    public String data(HttpSession session){

        // consumo de WCR/prelogin
        String url = "http://10.160.209.146:9084/CWCREST/services/resources/cobis/api/ref_laboral/customers/48/labor-references";

        String token = (String)session.getAttribute("BEARER_TOKEN");

        if(token==null){
            throw new RuntimeException("No hay token en sesion. Debes hacer el login primero");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        return response.getBody();
    }
}
