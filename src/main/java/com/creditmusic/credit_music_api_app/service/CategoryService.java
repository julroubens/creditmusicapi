package com.creditmusic.credit_music_api_app.service;

import com.creditmusic.credit_music_api_app.domain.Category;
import com.creditmusic.credit_music_api_app.domain.Song;
import com.creditmusic.credit_music_api_app.model.CategoryDTO;
import com.creditmusic.credit_music_api_app.repos.CategoryRepository;
import com.creditmusic.credit_music_api_app.repos.SongRepository;
import com.creditmusic.credit_music_api_app.util.NotFoundException;
import com.creditmusic.credit_music_api_app.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final SongRepository songRepository;

    public CategoryService(final CategoryRepository categoryRepository,
            final SongRepository songRepository) {
        this.categoryRepository = categoryRepository;
        this.songRepository = songRepository;
    }

    public List<CategoryDTO> findAll() {
        final List<Category> categorys = categoryRepository.findAll(Sort.by("id"));
        return categorys.stream()
                .map((category) -> mapToDTO(category, new CategoryDTO()))
                .toList();
    }

    public CategoryDTO get(final Long id) {
        return categoryRepository.findById(id)
                .map(category -> mapToDTO(category, new CategoryDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CategoryDTO categoryDTO) {
        final Category category = new Category();
        mapToEntity(categoryDTO, category);
        return categoryRepository.save(category).getId();
    }

    public void update(final Long id, final CategoryDTO categoryDTO) {
        final Category category = categoryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(categoryDTO, category);
        categoryRepository.save(category);
    }

    public void delete(final Long id) {
        categoryRepository.deleteById(id);
    }

    private CategoryDTO mapToDTO(final Category category, final CategoryDTO categoryDTO) {
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        return categoryDTO;
    }

    private Category mapToEntity(final CategoryDTO categoryDTO, final Category category) {
        category.setName(categoryDTO.getName());
        return category;
    }

    public String getReferencedWarning(final Long id) {
        final Category category = categoryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Song categorySong = songRepository.findFirstByCategory(category);
        if (categorySong != null) {
            return WebUtils.getMessage("category.song.category.referenced", categorySong.getId());
        }
        return null;
    }

}
