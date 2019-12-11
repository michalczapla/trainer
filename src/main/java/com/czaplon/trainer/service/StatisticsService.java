package com.czaplon.trainer.service;

import com.czaplon.trainer.model.User;
import com.czaplon.trainer.model.WorkoutHistory;
import com.czaplon.trainer.repository.WorkoutHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class StatisticsService {

    private WorkoutHistoryService workoutHistoryService;

    @Autowired
    public StatisticsService(WorkoutHistoryService workoutHistoryService) {
        this.workoutHistoryService = workoutHistoryService;
    }

    public Map<String, String> generateStatistics(Long workoutId, User user) {
        Map<String,String> statistics = new HashMap<>();
        // duration
        Optional<WorkoutHistory> firstWorkout = workoutHistoryService.findFirstByWorkoutIdAndUserOrderByDateAscIdAsc(workoutId,user);
        Optional<WorkoutHistory> lastWorkout = workoutHistoryService.findFirstByWorkoutIdAndUserOrderByDateDescIdDesc(workoutId,user);
        Integer workoutCount = workoutHistoryService.findAllByWorkoutIdAndUserAndWorkoutMade(workoutId,user,true).size();
        if (firstWorkout.isPresent()) {
            statistics.put("duration", String.valueOf(Duration.between(firstWorkout.get().getDate().atStartOfDay(), LocalDate.now().atStartOfDay()).toDays()));
        }
        if (firstWorkout.isPresent() && lastWorkout.isPresent()) {
            statistics.put("weightLoss", String.valueOf(firstWorkout.get().getWeight()-lastWorkout.get().getWeight()));
            statistics.put("waistLoss", String.valueOf(firstWorkout.get().getWaist()-lastWorkout.get().getWaist()));
            statistics.put("bmiIndex", String.valueOf(lastWorkout.get().getBmi()));
            statistics.put("bmiDescription", "Your BMI is considered as: "+lastWorkout.get().describeBMI());
        }
        statistics.put("workoutCount",String.valueOf(workoutCount));

//        historia z obrazkami
        Optional<WorkoutHistory> firstWorkoutImg = workoutHistoryService.findFirstByWorkoutIdAndUserAndImageNotNullOrderByDateAsc(workoutId,user);
        Optional<WorkoutHistory> lastWorkoutImg = workoutHistoryService.findFirstByWorkoutIdAndUserAndImageNotNullOrderByDateDesc(workoutId,user);

        if (workoutHistoryService.count()>1 &&
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
