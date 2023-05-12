package com.creditmusic.credit_music_api_app.service;

import com.creditmusic.credit_music_api_app.domain.Biography;
import com.creditmusic.credit_music_api_app.domain.Entity;
import com.creditmusic.credit_music_api_app.model.BiographyDTO;
import com.creditmusic.credit_music_api_app.repos.BiographyRepository;
import com.creditmusic.credit_music_api_app.repos.EntityRepository;
import com.creditmusic.credit_music_api_app.util.NotFoundException;
import com.creditmusic.credit_music_api_app.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class BiographyService {

    private final BiographyRepository biographyRepository;
    private final EntityRepository entityRepository;

    public BiographyService(final BiographyRepository biographyRepository,
            final EntityRepository entityRepository) {
        this.biographyRepository = biographyRepository;
        this.entityRepository = entityRepository;
    }

    public List<BiographyDTO> findAll() {
        final List<Biography> biographys = biographyRepository.findAll(Sort.by("id"));
        return biographys.stream()
                .map((biography) -> mapToDTO(biography, new BiographyDTO()))
                .toList();
    }

    public BiographyDTO get(final Long id) {
        return biographyRepository.findById(id)
                .map(biography -> mapToDTO(biography, new BiographyDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final BiographyDTO biographyDTO) {
        final Biography biography = new Biography();
        mapToEntity(biographyDTO, biography);
        return biographyRepository.save(biography).getId();
    }

    public void update(final Long id, final BiographyDTO biographyDTO) {
        final Biography biography = biographyRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(biographyDTO, biography);
        biographyRepository.save(biography);
    }

    public void delete(final Long id) {
        biographyRepository.deleteById(id);
    }

    private BiographyDTO mapToDTO(final Biography biography, final BiographyDTO biographyDTO) {
        biographyDTO.setId(biography.getId());
        biographyDTO.setName(biography.getName());
        biographyDTO.setDetail(biography.getDetail());
        return biographyDTO;
    }

    private Biography mapToEntity(final BiographyDTO biographyDTO, final Biography biography) {
        biography.setName(biographyDTO.getName());
        biography.setDetail(biographyDTO.getDetail());
        return biography;
    }

    public String getReferencedWarning(final Long id) {
        final Biography biography = biographyRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Entity biographyEntity = entityRepository.findFirstByBiography(biography);
        if (biographyEntity != null) {
            return WebUtils.getMessage("biography.entity.biography.referenced", biographyEntity.getId());
        }
        return null;
    }

}
