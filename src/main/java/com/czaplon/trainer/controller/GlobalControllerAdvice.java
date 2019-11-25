package com.czaplon.trainer.controller;

import com.czaplon.trainer.config.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.nio.file.Paths;

@ControllerAdvice
public class GlobalControllerAdvice {

    private StorageProperties storageProperties;

    @Autowired
    public GlobalControllerAdvice(StorageProperties storageProperties) {
        this.storageProperties = storageProperties;
    }

    @ModelAttribute("imagesPath")
    public String getImagesPath() {
       return "/" + Paths.get(storageProperties.getMappedLocation()).toString() + "/";
    }

}
