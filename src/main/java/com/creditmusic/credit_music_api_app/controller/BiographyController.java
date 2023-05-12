package com.creditmusic.credit_music_api_app.controller;

import com.creditmusic.credit_music_api_app.model.BiographyDTO;
import com.creditmusic.credit_music_api_app.service.BiographyService;
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
@RequestMapping("/biographys")
public class BiographyController {

    private final BiographyService biographyService;

    public BiographyController(final BiographyService biographyService) {
        this.biographyService = biographyService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("biographys", biographyService.findAll());
        return "biography/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("biography") final BiographyDTO biographyDTO) {
        return "biography/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("biography") @Valid final BiographyDTO biographyDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "biography/add";
        }
        biographyService.create(biographyDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("biography.create.success"));
        return "redirect:/biographys";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("biography", biographyService.get(id));
        return "biography/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("biography") @Valid final BiographyDTO biographyDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "biography/edit";
        }
        biographyService.update(id, biographyDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("biography.update.success"));
        return "redirect:/biographys";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = biographyService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            biographyService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("biography.delete.success"));
        }
        return "redirect:/biographys";
    }

}
