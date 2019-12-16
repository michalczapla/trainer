package com.czaplon.trainer.service;

import com.czaplon.trainer.model.User;
import com.czaplon.trainer.model.WorkoutHistory;
import com.czaplon.trainer.repository.WorkoutHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class WorkoutHistoryServiceImpl implements WorkoutHistoryService{

    private WorkoutHistoryRepository workoutHistoryRepository;
    private Logger logger = LoggerFactory.getLogger(WorkoutHistoryServiceImpl.class);

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

    @Override
    public List<WorkoutHistory> findAllByUser(User user) {
        return workoutHistoryRepository.findAllByUser(user);
    }

    @Override
    public LinkedList<WorkoutHistory> findAllByUserAndWorkoutIdOrderByDateDescIdDesc(User user, Long workoutId) {
        LinkedList<WorkoutHistory> list = workoutHistoryRepository.findAllByUserAndWorkoutIdOrderByDateDescIdDesc(user, workoutId);
        ListIterator<WorkoutHistory> iterator = list.listIterator();
        while (iterator.hasNext()) {
            WorkoutHistory current = iterator.next();
            WorkoutHistory nextWorkout = (iterator.hasNext()) ? iterator.next() : null;

            if (current!=null && nextWorkout!=null) {
                iterator.previous();
                current.setDaysSinceLastEntry(DAYS.between(nextWorkout.getDate(), current.getDate()));
            }
        }

        return list;
    }

    @Override
    public Optional<WorkoutHistory> findFirstByWorkoutIdAndUserOrderByDateDescIdDesc(Long workoutId, User user) {
        return workoutHistoryRepository.findFirstByWorkoutIdAndUserOrderByDateDescIdDesc(workoutId,user);
    }

    @Override
    public Optional<WorkoutHistory> findFirstByWorkoutIdAndUserOrderByDateAscIdAsc(Long workoutId, User user) {
        return workoutHistoryRepository.findFirstByWorkoutIdAndUserOrderByDateAscIdAsc(workoutId, user);
    }

    @Override
    public void recalculateBMI(User user) {
        List<WorkoutHistory> workouts = workoutHistoryRepository.findAllByUser(user);
        workouts.forEach(item->item.setBmi(calculateBMI(user.getHeight(),item.getWeight())));
        workoutHistoryRepository.saveAll(workouts);
        logger.info("Recalculated BMI for each entry for user :" + user.getUsername()+", ID: "+user.getId());
    }

    private static Float calculateBMI(Float height, Float weight) {
        height/=100;
        return (weight/(height*height));
    }
}
