package com.czaplon.trainer.service;

import com.czaplon.trainer.model.User;
import com.czaplon.trainer.model.WorkoutHistory;
import com.czaplon.trainer.repository.WorkoutHistoryRepository;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class StatisticsService {

    private WorkoutHistoryRepository workoutHistoryRepository;

    @Autowired
    public StatisticsService(WorkoutHistoryRepository workoutHistoryRepository) {
        this.workoutHistoryRepository = workoutHistoryRepository;
    }

    public Map<String, String> generateStatistics(Long workoutId, User user) {
        Map<String,String> statistics = new HashMap<>();
        // duration
        Optional<WorkoutHistory> firstWorkout = workoutHistoryRepository.findFirstByWorkoutIdAndUserOrderByDateAsc(workoutId,user);
        Optional<WorkoutHistory> lastWorkout = workoutHistoryRepository.findFirstByWorkoutIdAndUserOrderByDateDesc(workoutId,user);
        Integer workoutCount = workoutHistoryRepository.findAllByWorkoutIdAndUserAndWorkoutMade(workoutId,user,true).size();
        if (firstWorkout.isPresent()) {
            statistics.put("duration", String.valueOf(Duration.between(firstWorkout.get().getDate().atStartOfDay(), LocalDate.now().atStartOfDay()).toDays()));
        }
        if (firstWorkout.isPresent() && lastWorkout.isPresent()) {
            statistics.put("weightLoss", String.valueOf(firstWorkout.get().getWeight()-lastWorkout.get().getWeight()));
            statistics.put("waistLoss", String.valueOf(firstWorkout.get().getWaist()-lastWorkout.get().getWaist()));
        }
        statistics.put("workoutCount",String.valueOf(workoutCount));

//        historia z obrazkami
        Optional<WorkoutHistory> firstWorkoutImg = workoutHistoryRepository.findFirstByWorkoutIdAndUserAndImageNotNullOrderByDateAsc(workoutId,user);
        Optional<WorkoutHistory> lastWorkoutImg = workoutHistoryRepository.findFirstByWorkoutIdAndUserAndImageNotNullOrderByDateDesc(workoutId,user);

        if (workoutHistoryRepository.count()>1 &&
                firstWorkoutImg.isPresent() &&
                lastWorkoutImg.isPresent() &&
                firstWorkoutImg.get().getId()!=lastWorkoutImg.get().getId()) {
            statistics.put("firstImg",firstWorkoutImg.get().getImage());
            statistics.put("lastImg",lastWorkoutImg.get().getImage());
            statistics.put("firstImgDate",firstWorkoutImg.get().getDate().toString());
            statistics.put("lastImgDate",lastWorkoutImg.get().getDate().toString());
        }

//        logger.info(statistics.toString());
        return statistics;
    }

}
