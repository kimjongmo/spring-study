package com.spring.core.env;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

@Setter
@Getter
public class ConnectionProvider {
    private String driver;
    private String user;
    private String password;
    private String url;

    public ConnectionProvider() {
    }

    public void init(){

    }

    @Override
    public String toString() {
        return "ConnectionProvider{" +
                "driver='" + driver + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", url='" + url +
                '}';
    }
}
