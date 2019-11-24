package com.czaplon.trainer.controller;

import com.czaplon.trainer.config.SessionParameters;
import com.czaplon.trainer.model.User;
import com.czaplon.trainer.model.Workout;
import com.czaplon.trainer.model.WorkoutHistory;
import com.czaplon.trainer.repository.WorkoutHistoryRepository;
import com.czaplon.trainer.repository.WorkoutRepository;
import com.czaplon.trainer.service.storage.StorageException;
import com.czaplon.trainer.service.storage.StorageFileNotFoundException;
import com.czaplon.trainer.service.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/")
public class MainController {

    private Logger logger = LoggerFactory.getLogger(MainController.class);
    private WorkoutRepository workoutRepository;
    private WorkoutHistoryRepository workoutHistoryRepository;
    private SessionParameters sessionParameters;
    private StorageService storageService;


    @Autowired
    public MainController(WorkoutRepository workoutRepository, WorkoutHistoryRepository workoutHistoryRepository,SessionParameters sessionParameters,StorageService storageService) {
        this.workoutRepository = workoutRepository;
        this.workoutHistoryRepository = workoutHistoryRepository;
        this.sessionParameters=sessionParameters;
        this.storageService=storageService;
    }

    @ModelAttribute("workouts")
    public List<Workout> getWorkoutsModel(@AuthenticationPrincipal User user) {
        return workoutRepository.findAllByUser(user);
    }

    @ModelAttribute("workout")
    public Workout getWorkout() {
        return new Workout();
    }
    @ModelAttribute("workoutHistory")
    public WorkoutHistory getWorkoutHistory() {
        WorkoutHistory workoutHistory  = new WorkoutHistory();
        workoutHistory.setDate(LocalDate.now());
        return workoutHistory;
    }


        @GetMapping({"/"})
    public String getMain( @AuthenticationPrincipal User user, Model model, @RequestParam Optional<String> h) {

            if (h.isPresent()) {
                Long idLong = WorkoutController.convertStringToLong(h.get());
                if (idLong!=null && workoutRepository.findByIdAndUser(idLong,user).isPresent()) {
                    sessionParameters.setAndValidateCurrentWorkout(h.get());
                }
            }

        Map<String, String> names = new HashMap<>();
        names.put("username",user.getName());
        names.put("currentWorkout",sessionParameters.getCurrentWorkout(workoutRepository).getName());

        Map<String, String> statistics = generateStatistics(sessionParameters.getCurrentWorkout(),user);

        model.addAttribute("names",names);
        model.addAttribute("workoutHistoryList",workoutHistoryRepository.findAllByUserAndWorkoutIdOrderByDateDesc(user, sessionParameters.getCurrentWorkout()));
        model.addAttribute("statistics",statistics);
        return "main";
    }

    @PostMapping("/workout")
    public String addWorkout(@Valid Workout workout, BindingResult result, @AuthenticationPrincipal User user, RedirectAttributes redirectAttributes) {
        logger.info(workout.toString());
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.workout",result);
            redirectAttributes.addFlashAttribute("workout",workout);
            return "redirect:/";
        }
        workout.setUser(user);
        workoutRepository.save(workout);
        sessionParameters.setAndValidateCurrentWorkout(workout.getId().toString());
        logger.info(workout.toString());
        return "redirect:/";
    }

    @PostMapping("/workouthistory")
    public String addWorkoutHistory(@Valid WorkoutHistory workoutHistory, BindingResult result, @RequestParam MultipartFile imageFile, @AuthenticationPrincipal User user, RedirectAttributes redirectAttributes){
        logger.info(workoutHistory.toString());
        logger.info(imageFile.getContentType());
//        checking image
        if (!imageFile.isEmpty()) {
            try {
                workoutHistory.setImage(storageService.store(imageFile));
            } catch (IOException | StorageException | StorageFileNotFoundException e) {
                redirectAttributes.addFlashAttribute("imageError", e.getMessage());
                logger.warn(e.getMessage());
//                return "redirect:/";
            }
        }

//        checking Form validity
        if (result.hasErrors()) {
            logger.info("errors mapping workouthistory :(");
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.workoutHistory",result);
            redirectAttributes.addFlashAttribute("workoutHistory",workoutHistory);
            return "redirect:/";
        }
        workoutHistory.setUser(user);
        Workout currentWorkout = sessionParameters.getCurrentWorkout(workoutRepository);
        if (currentWorkout!=null) {
            workoutHistory.setWorkout(currentWorkout);
        }
        workoutHistoryRepository.save(workoutHistory);
        logger.info(workoutHistory.toString());
        return "redirect:/";
    }

    private Map<String, String> generateStatistics(Long workoutId, User user) {
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
