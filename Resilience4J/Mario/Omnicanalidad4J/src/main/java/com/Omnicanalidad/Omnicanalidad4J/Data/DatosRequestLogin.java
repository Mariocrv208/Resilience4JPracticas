package com.Omnicanalidad.Omnicanalidad4J.Data;
import com.Omnicanalidad.Omnicanalidad4J.Data.AttributesDto;

import java.util.jar.Attributes;

public class DatosRequestLogin {


    private String login;
    private String password;
    private AttributesDto attributes;

    public DatosRequestLogin(){
    }

    public DatosRequestLogin(String login, String password, AttributesDto attributes){
        this.login = login;
        this.password = password;
        this.attributes = attributes;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAttributes(AttributesDto attributes) {
        this.attributes = attributes;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public AttributesDto getAttributes() {
        return attributes;
    }
}


