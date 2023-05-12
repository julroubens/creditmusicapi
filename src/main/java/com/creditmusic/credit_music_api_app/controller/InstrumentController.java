package com.creditmusic.credit_music_api_app.controller;

import com.creditmusic.credit_music_api_app.domain.Musician;
import com.creditmusic.credit_music_api_app.model.InstrumentDTO;
import com.creditmusic.credit_music_api_app.repos.MusicianRepository;
import com.creditmusic.credit_music_api_app.service.InstrumentService;
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
@RequestMapping("/instruments")
public class InstrumentController {

    private final InstrumentService instrumentService;
    private final MusicianRepository musicianRepository;

    public InstrumentController(final InstrumentService instrumentService,
            final MusicianRepository musicianRepository) {
        this.instrumentService = instrumentService;
        this.musicianRepository = musicianRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("musicianValues", musicianRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Musician::getId, Musician::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("instruments", instrumentService.findAll());
        return "instrument/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("instrument") final InstrumentDTO instrumentDTO) {
        return "instrument/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("instrument") @Valid final InstrumentDTO instrumentDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "instrument/add";
        }
        instrumentService.create(instrumentDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("instrument.create.success"));
        return "redirect:/instruments";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("instrument", instrumentService.get(id));
        return "instrument/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("instrument") @Valid final InstrumentDTO instrumentDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "instrument/edit";
        }
        instrumentService.update(id, instrumentDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("instrument.update.success"));
        return "redirect:/instruments";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = instrumentService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            instrumentService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("instrument.delete.success"));
        }
        return "redirect:/instruments";
    }

}
