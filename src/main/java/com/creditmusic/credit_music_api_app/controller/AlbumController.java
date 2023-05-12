package com.creditmusic.credit_music_api_app.controller;

import com.creditmusic.credit_music_api_app.model.AlbumDTO;
import com.creditmusic.credit_music_api_app.service.AlbumService;
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
@RequestMapping("/albums")
public class AlbumController {

    private final AlbumService albumService;

    public AlbumController(final AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("albums", albumService.findAll());
        return "album/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("album") final AlbumDTO albumDTO) {
        return "album/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("album") @Valid final AlbumDTO albumDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "album/add";
        }
        albumService.create(albumDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("album.create.success"));
        return "redirect:/albums";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("album", albumService.get(id));
        return "album/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("album") @Valid final AlbumDTO albumDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "album/edit";
        }
        albumService.update(id, albumDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("album.update.success"));
        return "redirect:/albums";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = albumService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            albumService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("album.delete.success"));
        }
        return "redirect:/albums";
    }

}
