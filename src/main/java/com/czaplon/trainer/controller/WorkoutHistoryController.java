package com.czaplon.trainer.controller;

import com.czaplon.trainer.model.User;
import com.czaplon.trainer.model.WorkoutHistory;
import com.czaplon.trainer.repository.WorkoutHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/workouthistory")
public class WorkoutHistoryController {

    @Autowired
    private WorkoutHistoryRepository workoutHistoryRepository;


    @GetMapping("/edit")
    public String getEditWorkoutHistoryForm(Model model, @RequestParam Optional<String> h, @AuthenticationPrincipal User user) {
        Long IdLong = WorkoutController.convertStringToLong(h.get());
        if (IdLong==null) return "redirect:/";

        Optional<WorkoutHistory> workoutHistory = workoutHistoryRepository.findByIdAndUser(IdLong,user);
        model.addAttribute("workoutHisotryToEditName", workoutHistory.get().getDate());
        if (workoutHistory.isPresent()) {
            if (!model.containsAttribute("workoutHistory")) {
                model.addAttribute("workoutHistory", workoutHistory.get());

            }
        } else {
            return "redirect:/";
        }
        return "editWorkoutHistory";
    }

    @PostMapping("/edit/{id}")
    public String editWorkoutHistory(@Valid WorkoutHistory workoutHistory, BindingResult result, @PathVariable String id, RedirectAttributes redirectAttributes,@AuthenticationPrincipal User user) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.workoutHistory",result);
            redirectAttributes.addFlashAttribute("workoutHistory",workoutHistory);
            return "redirect:/workouthistory/edit?h="+id;
        }

        workoutHistoryRepository.save(workoutHistory);
        return "redirect:/";
    }

    @GetMapping("/delete")
    public String getDeleteWorkoutHistoryForm(@RequestParam Optional<String> h, Model model, @AuthenticationPrincipal User user) {
        Long idLong = WorkoutController.convertStringToLong(h.get());
        if (!h.isPresent() || idLong==null) {
            return "redirect:/";
        }
        Optional<WorkoutHistory> workoutToDelete = workoutHistoryRepository.findByIdAndUser(idLong, user);
        if (!workoutToDelete.isPresent()) return "redirect:/";

        model.addAttribute("dataToDelete",workoutToDelete.get().getDate());
        model.addAttribute("deletePath","/workouthistory/delete?h="+idLong);
        return "deleteConfirmation";

    }

    @PostMapping("/delete")
    @Transactional
    public String deleteWorkoutHistory(@RequestParam Optional<String> h, @AuthenticationPrincipal User user) {
        Long idLong = WorkoutController.convertStringToLong(h.get());
        if (!h.isPresent() || idLong==null) {
            return "redirect:/";
        }
        workoutHistoryRepository.deleteWorkoutHistoryByIdAndUser(idLong,user);
        return "redirect:/";
    }

}
