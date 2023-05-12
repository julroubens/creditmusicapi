package com.creditmusic.credit_music_api_app.service;

import com.creditmusic.credit_music_api_app.domain.Album;
import com.creditmusic.credit_music_api_app.domain.Category;
import com.creditmusic.credit_music_api_app.domain.Entity;
import com.creditmusic.credit_music_api_app.domain.Instrument;
import com.creditmusic.credit_music_api_app.domain.Song;
import com.creditmusic.credit_music_api_app.model.SongDTO;
import com.creditmusic.credit_music_api_app.repos.AlbumRepository;
import com.creditmusic.credit_music_api_app.repos.CategoryRepository;
import com.creditmusic.credit_music_api_app.repos.EntityRepository;
import com.creditmusic.credit_music_api_app.repos.InstrumentRepository;
import com.creditmusic.credit_music_api_app.repos.SongRepository;
import com.creditmusic.credit_music_api_app.util.NotFoundException;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Transactional
@Service
public class SongService {

    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;
    private final CategoryRepository categoryRepository;
    private final InstrumentRepository instrumentRepository;
    private final EntityRepository entityRepository;

    public SongService(final SongRepository songRepository, final AlbumRepository albumRepository,
            final CategoryRepository categoryRepository,
            final InstrumentRepository instrumentRepository,
            final EntityRepository entityRepository) {
        this.songRepository = songRepository;
        this.albumRepository = albumRepository;
        this.categoryRepository = categoryRepository;
        this.instrumentRepository = instrumentRepository;
        this.entityRepository = entityRepository;
    }

    public List<SongDTO> findAll() {
        final List<Song> songs = songRepository.findAll(Sort.by("id"));
        return songs.stream()
                .map((song) -> mapToDTO(song, new SongDTO()))
                .toList();
    }

    public SongDTO get(final Long id) {
        return songRepository.findById(id)
                .map(song -> mapToDTO(song, new SongDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final SongDTO songDTO) {
        final Song song = new Song();
        mapToEntity(songDTO, song);
        return songRepository.save(song).getId();
    }

    public void update(final Long id, final SongDTO songDTO) {
        final Song song = songRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(songDTO, song);
        songRepository.save(song);
    }

    public void delete(final Long id) {
        songRepository.deleteById(id);
    }

    private SongDTO mapToDTO(final Song song, final SongDTO songDTO) {
        songDTO.setId(song.getId());
        songDTO.setTitle(song.getTitle());
        songDTO.setArtist(song.getArtist());
        songDTO.setReleaseDate(song.getReleaseDate());
        songDTO.setImg(song.getImg());
        songDTO.setAlbum(song.getAlbum() == null ? null : song.getAlbum().getId());
        songDTO.setCategory(song.getCategory() == null ? null : song.getCategory().getId());
        songDTO.setInstrument(song.getInstrument() == null ? null : song.getInstrument().stream()
                .map(instrument -> instrument.getId())
                .toList());
        songDTO.setIntity(song.getIntity() == null ? null : song.getIntity().stream()
                .map(entity -> entity.getId())
                .toList());
        return songDTO;
    }

    private Song mapToEntity(final SongDTO songDTO, final Song song) {
        song.setTitle(songDTO.getTitle());
        song.setArtist(songDTO.getArtist());
        song.setReleaseDate(songDTO.getReleaseDate());
        song.setImg(songDTO.getImg());
        final Album album = songDTO.getAlbum() == null ? null : albumRepository.findById(songDTO.getAlbum())
                .orElseThrow(() -> new NotFoundException("album not found"));
        song.setAlbum(album);
        final Category category = songDTO.getCategory() == null ? null : categoryRepository.findById(songDTO.getCategory())
                .orElseThrow(() -> new NotFoundException("category not found"));
        song.setCategory(category);
        final List<Instrument> instrument = instrumentRepository.findAllById(
                songDTO.getInstrument() == null ? Collections.emptyList() : songDTO.getInstrument());
        if (instrument.size() != (songDTO.getInstrument() == null ? 0 : songDTO.getInstrument().size())) {
            throw new NotFoundException("one of instrument not found");
        }
        song.setInstrument(instrument.stream().collect(Collectors.toSet()));
        final List<Entity> intity = entityRepository.findAllById(
                songDTO.getIntity() == null ? Collections.emptyList() : songDTO.getIntity());
        if (intity.size() != (songDTO.getIntity() == null ? 0 : songDTO.getIntity().size())) {
            throw new NotFoundException("one of intity not found");
        }
        song.setIntity(intity.stream().collect(Collectors.toSet()));
        return song;
    }

}
