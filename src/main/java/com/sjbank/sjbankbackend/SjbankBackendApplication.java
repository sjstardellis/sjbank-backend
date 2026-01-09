package com.sjbank.sjbankbackend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SjbankBackendApplication {
    public static void main(String[] args) {


        // Load .env file before Spring Boot starts
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing() // Won't fail if .env is missing (useful for production)
                .load();

        // Set system properties from .env
        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue())
        );


        SpringApplication.run(SjbankBackendApplication.class, args);
    }
}