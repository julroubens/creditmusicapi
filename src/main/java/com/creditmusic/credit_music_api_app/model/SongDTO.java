package com.creditmusic.credit_music_api_app.model;

import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SongDTO {

    private Long id;

    @Size(max = 255)
    private String title;

    @Size(max = 255)
    private String artist;

    private LocalDate releaseDate;

    @Size(max = 255)
    private String img;

    private Long album;

    private Long category;

    private List<Long> instrument;

    private List<Long> intity;

}
