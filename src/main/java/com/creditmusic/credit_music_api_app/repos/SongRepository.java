package com.creditmusic.credit_music_api_app.repos;

import com.creditmusic.credit_music_api_app.domain.Album;
import com.creditmusic.credit_music_api_app.domain.Category;
import com.creditmusic.credit_music_api_app.domain.Entity;
import com.creditmusic.credit_music_api_app.domain.Instrument;
import com.creditmusic.credit_music_api_app.domain.Song;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SongRepository extends JpaRepository<Song, Long> {

    Song findFirstByIntity(Entity entity);

    Song findFirstByAlbum(Album album);

    Song findFirstByCategory(Category category);

    Song findFirstByInstrument(Instrument instrument);

}
