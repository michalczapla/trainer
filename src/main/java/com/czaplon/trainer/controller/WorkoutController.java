package com.czaplon.trainer.controller;

import com.czaplon.trainer.model.User;
import com.czaplon.trainer.model.Workout;
import com.czaplon.trainer.model.WorkoutHistory;
import com.czaplon.trainer.repository.WorkoutRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/workout")
public class WorkoutController {

    private Logger logger = LoggerFactory.getLogger(WorkoutController.class);

    private WorkoutRepository workoutRepository;

    @Autowired
    public WorkoutController(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    @GetMapping("/delete/{id}")
    public String deleteWorkoutConfirmation(@PathVariable String id, Model model) {
        Long idLong = convertStringToLong(id);
        if (idLong==null) return "redirect:/";

        Optional<Workout> workout = workoutRepository.findById(idLong);
        if (idLong!=null && workout.isPresent()) {
            model.addAttribute("dataToDelete", workout.get().getName());
            model.addAttribute("deletePath", "/workout/delete/" + idLong);
        } else {
            return "redirect:/";
        }
        return "deleteConfirmation";
    }

    @PostMapping("/delete/{id}")
    public String deleteWorkout(@PathVariable String id) {
        Long idLong = convertStringToLong(id);
        if (idLong==null) return "redirect:/";

        if (idLong!=null) {
            workoutRepository.deleteById(idLong);
        }

        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String editWorkoutForm(@PathVariable String id, Model model,@AuthenticationPrincipal User user){
        Long idLong = convertStringToLong(id);

        if (idLong==null) return "redirect:/";

        Optional<Workout> workout = workoutRepository.findByIdAndUser(idLong,user);
        if (idLong!=null && workout.isPresent()) {
            model.addAttribute("workoutToEditName", workout.get().getName());
            if (!model.containsAttribute("workoutToEdit"))
                model.addAttribute("workoutToEdit", workout.get());
        } else {
            return "redirect:/";
        }
        return "editWorkout";
    }

    @PostMapping("/edit/{id}")
    public String editWorkout(@PathVariable String id, @Valid Workout workoutToEdit, BindingResult result, RedirectAttributes attr){
        if (result.hasErrors()) {
            attr.addFlashAttribute("org.springframework.validation.BindingResult.workoutToEdit",result);
            attr.addFlashAttribute("workoutToEdit",workoutToEdit);
            return "redirect:/workout/edit/"+id;
        }

        Long idLong = convertStringToLong(id);
        if (idLong==null) return "redirect:/";
        Optional<Workout> workout = workoutRepository.findById(idLong);
        if (idLong!=null && workout.isPresent()) {
            workout.get().setName(workoutToEdit.getName());
            workoutRepository.save(workout.get());
        }
        return "redirect:/";
    }



    public static Long convertStringToLong(String id) {
        if (id==null) return null;
        Long idLong = null;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException ex) {

        }
        return idLong;
    }

}
