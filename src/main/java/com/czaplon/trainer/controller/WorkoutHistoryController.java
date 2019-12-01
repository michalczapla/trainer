package com.czaplon.trainer.controller;

import com.czaplon.trainer.model.User;
import com.czaplon.trainer.model.WorkoutHistory;
import com.czaplon.trainer.repository.WorkoutHistoryRepository;
import com.czaplon.trainer.service.storage.StorageException;
import com.czaplon.trainer.service.storage.StorageFileNotFoundException;
import com.czaplon.trainer.service.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/workouthistory")
public class WorkoutHistoryController {

    private Logger logger = LoggerFactory.getLogger(WorkoutHistoryController.class);
    private WorkoutHistoryRepository workoutHistoryRepository;
    private StorageService storageService;

    @Autowired
    public WorkoutHistoryController(WorkoutHistoryRepository workoutHistoryRepository, StorageService storageService) {
        this.workoutHistoryRepository = workoutHistoryRepository;
        this.storageService = storageService;
    }

    @GetMapping("/edit")
    public String getEditWorkoutHistoryForm(Model model, @RequestParam Optional<String> h, @AuthenticationPrincipal User user) {
        Long IdLong = WorkoutController.convertStringToLong(h.get());
        if (IdLong==null) return "redirect:/";

        Optional<WorkoutHistory> workoutHistory = workoutHistoryRepository.findByIdAndUser(IdLong,user);

        if (workoutHistory.isPresent()) {
            model.addAttribute("workoutHisotryToEditName", workoutHistory.get().getDate());

            if (!model.containsAttribute("workoutHistory")) {
                model.addAttribute("workoutHistory", workoutHistory.get());
            }
        } else {
            return "redirect:/";
        }
        return "editWorkoutHistory";
    }

    //z uwagi na zagniezdzenie formularzy musze adresowac na GET
   @GetMapping("/edit/{id}/clear")
    public String clearImage(@PathVariable String id, @AuthenticationPrincipal User user) {
        logger.info("Trying to clear image from id: "+id);
        if (!id.equals("")){
            Long IdLong = WorkoutController.convertStringToLong(id);
            if (IdLong==null) return "redirect:/";
            Optional<WorkoutHistory> workoutHistory = workoutHistoryRepository.findByIdAndUser(IdLong,user);

            if (workoutHistory.isPresent()) {
                storageService.markArchive(workoutHistory.get().getImage());
                workoutHistory.get().setImage(null);
                workoutHistoryRepository.save(workoutHistory.get());
            }
            return "redirect:/workouthistory/edit/?h=" + id;
        }
        return "redirect:/";
    }

    @PostMapping("/edit/{id}")
    public String editWorkoutHistory(@Valid WorkoutHistory workoutHistory, BindingResult result, @RequestParam MultipartFile imageFile, @PathVariable String id, RedirectAttributes redirectAttributes, @AuthenticationPrincipal User user) {
        boolean errors=false;
        //        checking image
        if (!imageFile.isEmpty()) {
            try {
                if (workoutHistory.getImage()!=null) storageService.delete(workoutHistory.getImage());
                workoutHistory.setImage(storageService.store(imageFile));
            } catch (IOException | StorageException | StorageFileNotFoundException e) {
                redirectAttributes.addFlashAttribute("imageError", e.getMessage());
                errors = true;
            }
        }

        logger.info("EDIT: "+ workoutHistory.toString());

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.workoutHistory",result);
            redirectAttributes.addFlashAttribute("workoutHistory",workoutHistory);
            errors=true;
        }

        if (errors) return "redirect:/workouthistory/edit?h="+id;
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
        Optional<WorkoutHistory> workoutToDelete = workoutHistoryRepository.findByIdAndUser(idLong,user);
        if (workoutToDelete.isPresent() && workoutToDelete.get().getImage()!=null) {
            storageService.markArchive(workoutToDelete.get().getImage());
        }
        workoutHistoryRepository.deleteWorkoutHistoryByIdAndUser(idLong,user);

        return "redirect:/";
    }

}
