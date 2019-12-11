package com.czaplon.trainer.service;

import com.czaplon.trainer.model.User;
import com.czaplon.trainer.model.WorkoutHistory;
import com.czaplon.trainer.repository.WorkoutHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface WorkoutHistoryService {

    WorkoutHistory save(WorkoutHistory workoutHistory);

    Long count();

    List<WorkoutHistory> findAllByUserAndWorkoutIdOrderByDateDesc(User user, Long workoutId);
    List<WorkoutHistory> findAllByUserAndWorkoutId(User user, Long workoutId);
    Optional<WorkoutHistory> findByIdAndUser(Long id, User user);

    Optional<WorkoutHistory> findFirstByWorkoutIdAndUserOrderByDateDesc(Long workoutId, User user);
    Optional<WorkoutHistory> findFirstByWorkoutIdAndUserAndImageNotNullOrderByDateDesc(Long workoutId, User user);

    Optional<WorkoutHistory> findFirstByWorkoutIdAndUserOrderByDateAsc(Long workoutId, User user);
    Optional<WorkoutHistory> findFirstByWorkoutIdAndUserAndImageNotNullOrderByDateAsc(Long workoutId, User user);

    List<WorkoutHistory> findAllByWorkoutIdAndUserAndWorkoutMade(Long workoutId,User user,Boolean workoutMade);


    void deleteWorkoutHistoryByIdAndUser(Long workoutHistoryId, User user);
}
