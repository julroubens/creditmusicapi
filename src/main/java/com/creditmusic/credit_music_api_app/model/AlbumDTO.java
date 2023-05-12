package com.creditmusic.credit_music_api_app.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AlbumDTO {

    private Long id;

    @Size(max = 255)
    private String groupName;

    @Size(max = 255)
    private String title;

    @Size(max = 255)
    private String releaseDate;

}
