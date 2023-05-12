package com.creditmusic.credit_music_api_app.rest;

import com.creditmusic.credit_music_api_app.model.BiographyDTO;
import com.creditmusic.credit_music_api_app.service.BiographyService;
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
@RequestMapping(value = "/api/biographys", produces = MediaType.APPLICATION_JSON_VALUE)
public class BiographyResource {

    private final BiographyService biographyService;

    public BiographyResource(final BiographyService biographyService) {
        this.biographyService = biographyService;
    }

    @GetMapping
    public ResponseEntity<List<BiographyDTO>> getAllBiographys() {
        return ResponseEntity.ok(biographyService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BiographyDTO> getBiography(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(biographyService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createBiography(
            @RequestBody @Valid final BiographyDTO biographyDTO) {
        final Long createdId = biographyService.create(biographyDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBiography(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final BiographyDTO biographyDTO) {
        biographyService.update(id, biographyDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteBiography(@PathVariable(name = "id") final Long id) {
        biographyService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
