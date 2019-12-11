package com.czaplon.trainer.controller;

import com.czaplon.trainer.config.StorageProperties;
import com.czaplon.trainer.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

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

    @ModelAttribute("loggedUser")
    public Map<String, String> getUserInfo(@AuthenticationPrincipal User user) {
        Map<String,String> userInfo = new HashMap<>();
        if (user!=null) {
            userInfo.put("name",user.getName());
        }

        return userInfo;
    }


    protected static Long convertStringToLong(String id) {
        if (id==null) return null;
        Long idLong = null;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException ex) {

        }
        return idLong;
    }

}
