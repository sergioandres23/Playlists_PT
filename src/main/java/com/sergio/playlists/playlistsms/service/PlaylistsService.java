package com.sergio.playlists.playlistsms.service;

import com.sergio.playlists.playlistsms.models.dto.PlaylistDTO;
import com.sergio.playlists.playlistsms.models.entities.PlaylistEntity;
import jakarta.transaction.Transactional;

import java.util.List;

public interface PlaylistsService {

    PlaylistDTO createPlaylist (PlaylistDTO playlistDTO);

    List<PlaylistEntity> getAllPlaylists();

    PlaylistEntity getPlaylist(String name);

    void deletePlaylist(String name);


}
