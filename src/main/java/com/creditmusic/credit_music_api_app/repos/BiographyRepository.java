package com.creditmusic.credit_music_api_app.repos;

import com.creditmusic.credit_music_api_app.domain.Biography;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BiographyRepository extends JpaRepository<Biography, Long> {
}
