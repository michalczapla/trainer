package com.czaplon.trainer.repository;

import com.czaplon.trainer.model.User;
import com.czaplon.trainer.model.Workout;
import org.hibernate.jdbc.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout,Long> {
    List<Workout> findAllByUser(User user);
    Optional<Workout> findByIdAndUser(Long id, User user);

}
