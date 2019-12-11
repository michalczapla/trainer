package com.czaplon.trainer.service;

import com.czaplon.trainer.model.User;
import com.czaplon.trainer.model.WorkoutHistory;
import com.czaplon.trainer.repository.WorkoutHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkoutHistoryServiceImpl implements WorkoutHistoryService{

    private WorkoutHistoryRepository workoutHistoryRepository;

    @Autowired
    public WorkoutHistoryServiceImpl(WorkoutHistoryRepository workoutHistoryRepository) {
        this.workoutHistoryRepository = workoutHistoryRepository;
    }

    @Override
    public WorkoutHistory save(WorkoutHistory workoutHistory) {
        workoutHistory.setBmi(calculateBMI(workoutHistory.getUser().getHeight(),workoutHistory.getWeight()));
        return workoutHistoryRepository.save(workoutHistory);
    }

    @Override
    public Long count() {
        return workoutHistoryRepository.count();
    }

    @Override
    public List<WorkoutHistory> findAllByUserAndWorkoutIdOrderByDateDesc(User user, Long workoutId) {
        return workoutHistoryRepository.findAllByUserAndWorkoutIdOrderByDateDesc(user, workoutId);
    }

    @Override
    public List<WorkoutHistory> findAllByUserAndWorkoutId(User user, Long workoutId) {
        return workoutHistoryRepository.findAllByUserAndWorkoutId(user, workoutId);
    }

    @Override
    public Optional<WorkoutHistory> findByIdAndUser(Long id, User user) {
        return workoutHistoryRepository.findByIdAndUser(id, user);
    }

    @Override
    public Optional<WorkoutHistory> findFirstByWorkoutIdAndUserOrderByDateDesc(Long workoutId, User user) {
        return workoutHistoryRepository.findFirstByWorkoutIdAndUserOrderByDateDesc(workoutId, user);
    }

    @Override
    public Optional<WorkoutHistory> findFirstByWorkoutIdAndUserAndImageNotNullOrderByDateDesc(Long workoutId, User user) {
        return workoutHistoryRepository.findFirstByWorkoutIdAndUserAndImageNotNullOrderByDateDesc(workoutId, user);
    }

    @Override
    public Optional<WorkoutHistory> findFirstByWorkoutIdAndUserOrderByDateAsc(Long workoutId, User user) {
        return workoutHistoryRepository.findFirstByWorkoutIdAndUserOrderByDateAsc(workoutId, user);
    }

    @Override
    public Optional<WorkoutHistory> findFirstByWorkoutIdAndUserAndImageNotNullOrderByDateAsc(Long workoutId, User user) {
        return workoutHistoryRepository.findFirstByWorkoutIdAndUserAndImageNotNullOrderByDateAsc(workoutId, user);
    }

    @Override
    public List<WorkoutHistory> findAllByWorkoutIdAndUserAndWorkoutMade(Long workoutId, User user, Boolean workoutMade) {
        return workoutHistoryRepository.findAllByWorkoutIdAndUserAndWorkoutMade(workoutId, user, workoutMade);
    }

    @Override
    public void deleteWorkoutHistoryByIdAndUser(Long workoutHistoryId, User user) {
        workoutHistoryRepository.deleteWorkoutHistoryByIdAndUser(workoutHistoryId,user);
    }

    private static Float calculateBMI(Float height, Float weight) {
        height/=100;
        return (weight/(height*height));
    }
}
