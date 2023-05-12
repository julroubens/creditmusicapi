package com.creditmusic.credit_music_api_app.controller;

import com.creditmusic.credit_music_api_app.domain.Album;
import com.creditmusic.credit_music_api_app.domain.Category;
import com.creditmusic.credit_music_api_app.domain.Entity;
import com.creditmusic.credit_music_api_app.domain.Instrument;
import com.creditmusic.credit_music_api_app.model.SongDTO;
import com.creditmusic.credit_music_api_app.repos.AlbumRepository;
import com.creditmusic.credit_music_api_app.repos.CategoryRepository;
import com.creditmusic.credit_music_api_app.repos.EntityRepository;
import com.creditmusic.credit_music_api_app.repos.InstrumentRepository;
import com.creditmusic.credit_music_api_app.service.SongService;
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
@RequestMapping("/songs")
public class SongController {

    private final SongService songService;
    private final AlbumRepository albumRepository;
    private final CategoryRepository categoryRepository;
    private final InstrumentRepository instrumentRepository;
    private final EntityRepository entityRepository;

    public SongController(final SongService songService, final AlbumRepository albumRepository,
            final CategoryRepository categoryRepository,
            final InstrumentRepository instrumentRepository,
            final EntityRepository entityRepository) {
        this.songService = songService;
        this.albumRepository = albumRepository;
        this.categoryRepository = categoryRepository;
        this.instrumentRepository = instrumentRepository;
        this.entityRepository = entityRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("albumValues", albumRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Album::getId, Album::getGroupName)));
        model.addAttribute("categoryValues", categoryRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Category::getId, Category::getName)));
        model.addAttribute("instrumentValues", instrumentRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Instrument::getId, Instrument::getName)));
        model.addAttribute("intityValues", entityRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Entity::getId, Entity::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("songs", songService.findAll());
        return "song/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("song") final SongDTO songDTO) {
        return "song/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("song") @Valid final SongDTO songDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "song/add";
        }
        songService.create(songDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("song.create.success"));
        return "redirect:/songs";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("song", songService.get(id));
        return "song/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("song") @Valid final SongDTO songDTO, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "song/edit";
        }
        songService.update(id, songDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("song.update.success"));
        return "redirect:/songs";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        songService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("song.delete.success"));
        return "redirect:/songs";
    }

}
