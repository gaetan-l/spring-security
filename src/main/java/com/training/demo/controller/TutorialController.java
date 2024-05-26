package com.training.demo.controller;

import com.training.demo.dto.TutorialDto;
import com.training.demo.exception.TutorialNotFoundException;
import com.training.demo.model.Tutorial;
import com.training.demo.service.TutorialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/v1/tutorial")
@RequiredArgsConstructor
public class TutorialController {
    private final TutorialService tutorialService;

    @GetMapping("/{id}")
    public String getTutorial(final Model model, final @PathVariable(value="id") Long id) {
        final List<TutorialDto> tutorials = new ArrayList<>();
        final Optional<TutorialDto> tutorial = tutorialService.get(id);
        tutorial.ifPresent(tutorials::add);
        model.addAttribute("tutorials", tutorials);
        return "tuto-display";
    }

    @GetMapping
    public String getTutorials(final Model model) {
        final List<TutorialDto> tutorials = tutorialService.getAll();
        model.addAttribute("tutorials", tutorials);
        return "tuto-display";
    }

    @GetMapping("/new")
    public String startTutorialCreation(final Model model) {
        model.addAttribute("action", "/tutorial/new");
        model.addAttribute("method", "POST");
        model.addAttribute("tutorial", new TutorialDto());
        return "tuto-edit";
    }

    @GetMapping("/{id}/edit")
    public String startTutorialEdition(final Model model, final @PathVariable(value="id") Long id) {
        model.addAttribute("action", "/tutorial/" + id + "/edit");
        model.addAttribute("method", "PUT");
        final Optional<TutorialDto> tutorial = tutorialService.get(id);
        model.addAttribute("tutorial", tutorial.orElseGet(TutorialDto::new));
        return "tuto-edit";
    }

    @PostMapping("/new")
    public String createTutorial(final Model model, final TutorialDto creationDto) {
        tutorialService.create(creationDto);
        return "redirect:/tutorial";
    }

    @PostMapping("/{id}/delete")
    public String deleteTutorial(@PathVariable("id") final Long id) {
        final Optional<TutorialDto> foundTutorial = tutorialService.get(id);

        if (foundTutorial.isEmpty()) {
            throw new TutorialNotFoundException(id);
        }
        else {
            tutorialService.delete(id);
            return "redirect:/tutorial";
        }
    }

    @PostMapping("/{id}/edit")
    public String updateTutorial(
            final Model model, @PathVariable("id") final Long id, final TutorialDto paramTutorial) {

        final Optional<TutorialDto> foundTutorial = tutorialService.get(id);

        if (foundTutorial.isEmpty()) {
            throw new TutorialNotFoundException(id);
        }
        else {
            TutorialDto updated = foundTutorial.get();
            updated.setTitle(paramTutorial.getTitle());
            updated.setContent(paramTutorial.getContent());
            updated.setDescription(paramTutorial.getDescription());
            updated.setCreateAt(paramTutorial.getCreateAt());
            updated.setCategory(paramTutorial.getCategory());
            tutorialService.update(updated);
            return "redirect:/tutorial";
        }
    }
}