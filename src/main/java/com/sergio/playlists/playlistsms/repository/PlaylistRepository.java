package com.sergio.playlists.playlistsms.repository;


import com.sergio.playlists.playlistsms.models.entities.PlaylistEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<PlaylistEntity, Long> {

    Optional<PlaylistEntity> findByName(String name);
    void deleteByName(String name);
}
