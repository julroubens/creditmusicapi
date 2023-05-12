package com.creditmusic.credit_music_api_app.service;

import com.creditmusic.credit_music_api_app.domain.Album;
import com.creditmusic.credit_music_api_app.domain.Song;
import com.creditmusic.credit_music_api_app.model.AlbumDTO;
import com.creditmusic.credit_music_api_app.repos.AlbumRepository;
import com.creditmusic.credit_music_api_app.repos.SongRepository;
import com.creditmusic.credit_music_api_app.util.NotFoundException;
import com.creditmusic.credit_music_api_app.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final SongRepository songRepository;

    public AlbumService(final AlbumRepository albumRepository,
            final SongRepository songRepository) {
        this.albumRepository = albumRepository;
        this.songRepository = songRepository;
    }

    public List<AlbumDTO> findAll() {
        final List<Album> albums = albumRepository.findAll(Sort.by("id"));
        return albums.stream()
                .map((album) -> mapToDTO(album, new AlbumDTO()))
                .toList();
    }

    public AlbumDTO get(final Long id) {
        return albumRepository.findById(id)
                .map(album -> mapToDTO(album, new AlbumDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final AlbumDTO albumDTO) {
        final Album album = new Album();
        mapToEntity(albumDTO, album);
        return albumRepository.save(album).getId();
    }

    public void update(final Long id, final AlbumDTO albumDTO) {
        final Album album = albumRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(albumDTO, album);
        albumRepository.save(album);
    }

    public void delete(final Long id) {
        albumRepository.deleteById(id);
    }

    private AlbumDTO mapToDTO(final Album album, final AlbumDTO albumDTO) {
        albumDTO.setId(album.getId());
        albumDTO.setGroupName(album.getGroupName());
        albumDTO.setTitle(album.getTitle());
        albumDTO.setReleaseDate(album.getReleaseDate());
        return albumDTO;
    }

    private Album mapToEntity(final AlbumDTO albumDTO, final Album album) {
        album.setGroupName(albumDTO.getGroupName());
        album.setTitle(albumDTO.getTitle());
        album.setReleaseDate(albumDTO.getReleaseDate());
        return album;
    }

    public String getReferencedWarning(final Long id) {
        final Album album = albumRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Song albumSong = songRepository.findFirstByAlbum(album);
        if (albumSong != null) {
            return WebUtils.getMessage("album.song.album.referenced", albumSong.getId());
        }
        return null;
    }

}
