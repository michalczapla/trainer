package com.czaplon.trainer.repository;

import com.czaplon.trainer.model.User;
import com.czaplon.trainer.model.Workout;
import com.czaplon.trainer.model.WorkoutHistory;
import org.hibernate.jdbc.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutHistoryRepository extends JpaRepository<WorkoutHistory,Long> {
    List<WorkoutHistory> findAllByUserAndWorkoutIdOrderByDateDesc(User user, Long workoutId);
    List<WorkoutHistory> findAllByUserAndWorkoutId(User user, Long workoutId);
    Optional<WorkoutHistory> findByIdAndUser(Long id,User user);

    Optional<WorkoutHistory> findFirstByWorkoutIdAndUserOrderByDateDesc(Long workoutId, User user);
    Optional<WorkoutHistory> findFirstByWorkoutIdAndUserOrderByDateAsc(Long workoutId, User user);
    List<WorkoutHistory> findAllByWorkoutIdAndUserAndWorkoutMade(Long workoutId,User user,Boolean workoutMade);

    void deleteWorkoutHistoryByIdAndUser(Long workoutHistoryId, User user);
}
