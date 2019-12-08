package com.czaplon.trainer;

import com.czaplon.trainer.service.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TrainerApplication { // extends SpringBootServletInitializer

//    //konfiguracja pod *war
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return builder.sources(TrainerApplication.class);
//    }

    public static void main(String[] args) {
        SpringApplication.run(TrainerApplication.class, args);
    }

    @Bean
    CommandLineRunner init(StorageService storageService){
        return args -> {
//            storageService.deleteAll();
            storageService.init();
        };
    }
}
