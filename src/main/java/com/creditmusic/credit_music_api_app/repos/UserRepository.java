package com.creditmusic.credit_music_api_app.repos;

import com.creditmusic.credit_music_api_app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
}
