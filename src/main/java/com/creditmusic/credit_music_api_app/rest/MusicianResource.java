package com.creditmusic.credit_music_api_app.rest;

import com.creditmusic.credit_music_api_app.model.MusicianDTO;
import com.creditmusic.credit_music_api_app.service.MusicianService;
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
@RequestMapping(value = "/api/musicians", produces = MediaType.APPLICATION_JSON_VALUE)
public class MusicianResource {

    private final MusicianService musicianService;

    public MusicianResource(final MusicianService musicianService) {
        this.musicianService = musicianService;
    }

    @GetMapping
    public ResponseEntity<List<MusicianDTO>> getAllMusicians() {
        return ResponseEntity.ok(musicianService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MusicianDTO> getMusician(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(musicianService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createMusician(@RequestBody @Valid final MusicianDTO musicianDTO) {
        final Long createdId = musicianService.create(musicianDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMusician(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final MusicianDTO musicianDTO) {
        musicianService.update(id, musicianDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteMusician(@PathVariable(name = "id") final Long id) {
        musicianService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
