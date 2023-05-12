package com.creditmusic.credit_music_api_app.controller;

import com.creditmusic.credit_music_api_app.model.MusicianDTO;
import com.creditmusic.credit_music_api_app.service.MusicianService;
import com.creditmusic.credit_music_api_app.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/musicians")
public class MusicianController {

    private final MusicianService musicianService;

    public MusicianController(final MusicianService musicianService) {
        this.musicianService = musicianService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("musicians", musicianService.findAll());
        return "musician/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("musician") final MusicianDTO musicianDTO) {
        return "musician/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("musician") @Valid final MusicianDTO musicianDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "musician/add";
        }
        musicianService.create(musicianDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("musician.create.success"));
        return "redirect:/musicians";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("musician", musicianService.get(id));
        return "musician/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("musician") @Valid final MusicianDTO musicianDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "musician/edit";
        }
        musicianService.update(id, musicianDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("musician.update.success"));
        return "redirect:/musicians";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = musicianService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            musicianService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("musician.delete.success"));
        }
        return "redirect:/musicians";
    }

}
