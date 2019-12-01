package com.czaplon.trainer.config;

import com.czaplon.trainer.model.User;
import com.czaplon.trainer.model.Workout;
import com.czaplon.trainer.repository.WorkoutRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.Optional;

public class SessionParameters {
    private Long currentWorkout;

    public SessionParameters() {
        currentWorkout=null;
    }

    public Long getCurrentWorkout() {
        return (currentWorkout==null)? 1 : currentWorkout;
    }

    public Workout getCurrentWorkout(WorkoutRepository workoutRepository, User user) {
        if (currentWorkout==null) {
            Optional<Workout> workout = workoutRepository.getFirstByUserOrderByIdAsc(user);
            if (workout.isPresent()){
                currentWorkout = workout.get().getId();
                return workout.get();
            } else {
                return null;
            }
        }
        Optional<Workout> currentWorkout = workoutRepository.findByIdAndUser(getCurrentWorkout(),user);
        if (currentWorkout.isPresent()) {
            return currentWorkout.get();
        } else {
            return null;
        }
    }

    public void setLastWorkout(WorkoutRepository workoutRepository,User user) {
        Optional<Workout> workout = workoutRepository.getFirstByUserOrderByIdDesc(user);
        if (workout.isPresent()) {
            currentWorkout = workout.get().getId();
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
