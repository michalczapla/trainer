package com.czaplon.trainer.service.storage;

import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;


public class TIKAFileAnalizer {

    static public String checkMIMEType (MultipartFile file) throws FileNotFoundException {
        String mimetype="";
        try {
            Detector detector = new DefaultDetector();
            mimetype = detector.detect(TikaInputStream.get(file.getInputStream()), new Metadata()).toString();
        } catch (IOException e) {
            throw new StorageException("Cannot read file");
        }
        return mimetype;
    }

}
