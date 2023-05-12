package com.creditmusic.credit_music_api_app.controller;

import com.creditmusic.credit_music_api_app.domain.Biography;
import com.creditmusic.credit_music_api_app.model.EntityDTO;
import com.creditmusic.credit_music_api_app.repos.BiographyRepository;
import com.creditmusic.credit_music_api_app.service.EntityService;
import com.creditmusic.credit_music_api_app.util.CustomCollectors;
import com.creditmusic.credit_music_api_app.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
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
@RequestMapping("/entitys")
public class EntityController {

    private final EntityService entityService;
    private final BiographyRepository biographyRepository;

    public EntityController(final EntityService entityService,
            final BiographyRepository biographyRepository) {
        this.entityService = entityService;
        this.biographyRepository = biographyRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("biographyValues", biographyRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Biography::getId, Biography::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("entitys", entityService.findAll());
        return "entity/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("entity") final EntityDTO entityDTO) {
        return "entity/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("entity") @Valid final EntityDTO entityDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "entity/add";
        }
        entityService.create(entityDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("entity.create.success"));
        return "redirect:/entitys";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("entity", entityService.get(id));
        return "entity/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("entity") @Valid final EntityDTO entityDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "entity/edit";
        }
        entityService.update(id, entityDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("entity.update.success"));
        return "redirect:/entitys";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = entityService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            entityService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("entity.delete.success"));
        }
        return "redirect:/entitys";
    }

}
