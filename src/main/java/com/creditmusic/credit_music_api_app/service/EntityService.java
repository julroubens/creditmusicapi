package com.creditmusic.credit_music_api_app.service;

import com.creditmusic.credit_music_api_app.domain.Biography;
import com.creditmusic.credit_music_api_app.domain.Entity;
import com.creditmusic.credit_music_api_app.domain.Song;
import com.creditmusic.credit_music_api_app.model.EntityDTO;
import com.creditmusic.credit_music_api_app.repos.BiographyRepository;
import com.creditmusic.credit_music_api_app.repos.EntityRepository;
import com.creditmusic.credit_music_api_app.repos.SongRepository;
import com.creditmusic.credit_music_api_app.util.NotFoundException;
import com.creditmusic.credit_music_api_app.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class EntityService {

    private final EntityRepository entityRepository;
    private final BiographyRepository biographyRepository;
    private final SongRepository songRepository;

    public EntityService(final EntityRepository entityRepository,
            final BiographyRepository biographyRepository, final SongRepository songRepository) {
        this.entityRepository = entityRepository;
        this.biographyRepository = biographyRepository;
        this.songRepository = songRepository;
    }

    public List<EntityDTO> findAll() {
        final List<Entity> entitys = entityRepository.findAll(Sort.by("id"));
        return entitys.stream()
                .map((entity) -> mapToDTO(entity, new EntityDTO()))
                .toList();
    }

    public EntityDTO get(final Long id) {
        return entityRepository.findById(id)
                .map(entity -> mapToDTO(entity, new EntityDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final EntityDTO entityDTO) {
        final Entity entity = new Entity();
        mapToEntity(entityDTO, entity);
        return entityRepository.save(entity).getId();
    }

    public void update(final Long id, final EntityDTO entityDTO) {
        final Entity entity = entityRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(entityDTO, entity);
        entityRepository.save(entity);
    }

    public void delete(final Long id) {
        entityRepository.deleteById(id);
    }

    private EntityDTO mapToDTO(final Entity entity, final EntityDTO entityDTO) {
        entityDTO.setId(entity.getId());
        entityDTO.setName(entity.getName());
        entityDTO.setBiography(entity.getBiography() == null ? null : entity.getBiography().getId());
        return entityDTO;
    }

    private Entity mapToEntity(final EntityDTO entityDTO, final Entity entity) {
        entity.setName(entityDTO.getName());
        final Biography biography = entityDTO.getBiography() == null ? null : biographyRepository.findById(entityDTO.getBiography())
                .orElseThrow(() -> new NotFoundException("biography not found"));
        entity.setBiography(biography);
        return entity;
    }

    public String getReferencedWarning(final Long id) {
        final Entity entity = entityRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Song intitySong = songRepository.findFirstByIntity(entity);
        if (intitySong != null) {
            return WebUtils.getMessage("entity.song.intity.referenced", intitySong.getId());
        }
        return null;
    }

}
