package com.czaplon.trainer.config;

import com.czaplon.trainer.model.Workout;
import com.czaplon.trainer.repository.WorkoutRepository;

import java.util.Optional;

public class SessionParameters {
    private Long currentWorkout;

    public SessionParameters() {
        currentWorkout=null;
    }

    public Long getCurrentWorkout() {
        return (currentWorkout==null)? 1 : currentWorkout;
    }

    public Workout getCurrentWorkout(WorkoutRepository workoutRepository) {
        Optional<Workout> currentWorkout = workoutRepository.findById(getCurrentWorkout());
        if (currentWorkout.isPresent()) {
            return currentWorkout.get();
        } else {
            return null;
        }
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
