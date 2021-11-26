package com.libre.librerias;

import com.libre.librerias.servicios.UserSer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class LibreriasApplication {
    
    @Autowired
    UserSer useS;

    public static void main(String[] args) {
        SpringApplication.run(LibreriasApplication.class, args);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(useS)
                .passwordEncoder(new BCryptPasswordEncoder());

    }

}
