package com.creditmusic.credit_music_api_app.rest;

import com.creditmusic.credit_music_api_app.model.SongDTO;
import com.creditmusic.credit_music_api_app.service.SongService;
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
@RequestMapping(value = "/api/songs", produces = MediaType.APPLICATION_JSON_VALUE)
public class SongResource {

    private final SongService songService;

    public SongResource(final SongService songService) {
        this.songService = songService;
    }

    @GetMapping
    public ResponseEntity<List<SongDTO>> getAllSongs() {
        return ResponseEntity.ok(songService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongDTO> getSong(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(songService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createSong(@RequestBody @Valid final SongDTO songDTO) {
        final Long createdId = songService.create(songDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSong(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final SongDTO songDTO) {
        songService.update(id, songDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteSong(@PathVariable(name = "id") final Long id) {
        songService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
