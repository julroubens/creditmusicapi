package com.creditmusic.credit_music_api_app.rest;

import com.creditmusic.credit_music_api_app.model.AlbumDTO;
import com.creditmusic.credit_music_api_app.service.AlbumService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/albums", produces = MediaType.APPLICATION_JSON_VALUE)
public class AlbumResource {

    private final AlbumService albumService;

    public AlbumResource(final AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping
    public ResponseEntity<List<AlbumDTO>> getAllAlbums() {
        return ResponseEntity.ok(albumService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbumDTO> getAlbum(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(albumService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createAlbum(@RequestBody @Valid final AlbumDTO albumDTO) {
        final Long createdId = albumService.create(albumDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAlbum(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final AlbumDTO albumDTO) {
        albumService.update(id, albumDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAlbum(@PathVariable(name = "id") final Long id) {
        albumService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
