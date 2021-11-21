package com.telerik.peer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication

public class PeerApplication {
    public static void main(String[] args) {
        SpringApplication.run(PeerApplication.class, args);
    }

}
