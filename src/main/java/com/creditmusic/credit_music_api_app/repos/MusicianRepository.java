package com.creditmusic.credit_music_api_app.repos;

import com.creditmusic.credit_music_api_app.domain.Musician;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MusicianRepository extends JpaRepository<Musician, Long> {
}
