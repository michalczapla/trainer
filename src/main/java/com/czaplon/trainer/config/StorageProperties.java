package com.czaplon.trainer.config;


public class StorageProperties {
    /**
     * Folder location for storing files
     */
    private String location = "upload-dir";
    private String mappedLocation = "imgs";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMappedLocation() {
        return mappedLocation;
    }

    public void setMappedLocation(String mappedLocation) {
        this.mappedLocation = mappedLocation;
    }
}
