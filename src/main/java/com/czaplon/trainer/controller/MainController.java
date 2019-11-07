package com.czaplon.trainer.controller;

import com.czaplon.trainer.config.SessionParameters;
import com.czaplon.trainer.model.User;
import com.czaplon.trainer.model.Workout;
import com.czaplon.trainer.model.WorkoutHistory;
import com.czaplon.trainer.repository.WorkoutHistoryRepository;
import com.czaplon.trainer.repository.WorkoutRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class MainController {

    private Logger logger = LoggerFactory.getLogger(MainController.class);
    private WorkoutRepository workoutRepository;
    private WorkoutHistoryRepository workoutHistoryRepository;
    private SessionParameters sessionParameters;

    @Autowired
    public MainController(WorkoutRepository workoutRepository, WorkoutHistoryRepository workoutHistoryRepository,SessionParameters sessionParameters) {
        this.workoutRepository = workoutRepository;
        this.workoutHistoryRepository = workoutHistoryRepository;
        this.sessionParameters=sessionParameters;
    }

    @ModelAttribute("workouts")
    public List<Workout> getWorkoutsModel(@AuthenticationPrincipal User user) {
        return workoutRepository.findAllByUser(user);
    }
    @ModelAttribute("username")
    public String getUsernameModel(@AuthenticationPrincipal User user) {
        return user.getName();
    }
    @ModelAttribute("workout")
    public Workout getWorkout() {
        return new Workout();
    }
    @ModelAttribute("workoutHistory")
    public WorkoutHistory getWorkoutHistory() {
        return new WorkoutHistory();
    }


        @GetMapping({"/"})
    public String getMain( @AuthenticationPrincipal User user, Model model, @RequestParam Optional<String> h) {

            if (h.isPresent()) {
                sessionParameters.setAndValidateCurrentWorkout(h.get());
            }

        model.addAttribute("workoutHistoryList",workoutHistoryRepository.findAllByUserAndWorkoutId(user, sessionParameters.getCurrentWorkout()));
        //model.addAttribute("workoutHistoryList",getWorkoutHistoryListModel(user,new Workout()));
        return "main";
    }

    @PostMapping("/workout")
    public String addWorkout(@Valid Workout workout, BindingResult result, @AuthenticationPrincipal User user) {
        logger.info(workout.toString());
        if (result.hasErrors()) {
            return "main";
        }
        workout.setUser(user);
        workoutRepository.save(workout);
        logger.info(workout.toString());
        return "redirect:/";
    }

    @PostMapping("/workouthistory")
    public String addWorkoutHistory(@Valid WorkoutHistory workoutHistory, BindingResult result, @AuthenticationPrincipal User user){
        logger.info(workoutHistory.toString());
        if (result.hasErrors()) {
            logger.info("errors mapping workouthistory :(");
            return "main";
        }
        workoutHistory.setUser(user);
        Optional<Workout> currentWorkout = workoutRepository.findById(sessionParameters.getCurrentWorkout());
        if (currentWorkout.isPresent()) {
            workoutHistory.setWorkout(currentWorkout.get());
        }
        workoutHistoryRepository.save(workoutHistory);
        logger.info(workoutHistory.toString());
        return "redirect:/";
    }

}
