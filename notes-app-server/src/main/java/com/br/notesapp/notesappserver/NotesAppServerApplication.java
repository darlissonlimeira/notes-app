package com.br.notesapp.notesappserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class NotesAppServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotesAppServerApplication.class, args);
    }
}
