package com.czaplon.trainer.config;

public class SessionParameters {
    private Long currentWorkout;

    public SessionParameters() {
        currentWorkout=null;
    }

    public Long getCurrentWorkout() {
        return (currentWorkout==null)? 1 : currentWorkout;
    }

    public void setCurrentWorkout(Long currentWorkout) {
        this.currentWorkout = currentWorkout;
    }

    public void setAndValidateCurrentWorkout(String newValue) {
        Long parsedValue = null;
        try {
            parsedValue = Long.parseLong(newValue);
        } catch (NumberFormatException ex) {
            return;
        }
        this.currentWorkout=parsedValue;
    }
}
