package com.czaplon.trainer;

import com.czaplon.trainer.service.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TrainerApplication {

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
