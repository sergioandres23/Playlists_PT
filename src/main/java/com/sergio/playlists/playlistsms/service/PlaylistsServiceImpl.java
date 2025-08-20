package com.sergio.playlists.playlistsms.service;

import com.sergio.playlists.playlistsms.mappers.PlaylistMapper;
import com.sergio.playlists.playlistsms.models.dto.PlaylistDTO;
import com.sergio.playlists.playlistsms.models.entities.PlaylistEntity;
import com.sergio.playlists.playlistsms.repository.PlaylistRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PlaylistsServiceImpl implements PlaylistsService{

    private final PlaylistRepository playlistRepository;
    private final PlaylistMapper playlistMapper;

    public PlaylistsServiceImpl(PlaylistRepository playlistRepository, PlaylistMapper playlistMapper) {
        this.playlistRepository = playlistRepository;
        this.playlistMapper = playlistMapper;
    }

    @Override
    @Transactional
    public PlaylistDTO createPlaylist(PlaylistDTO playlist) {

        String name = playlist.getName();
        if (name == null || name.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Playlist name cannot be null or empty");
        }
        if (playlistRepository.findByName(name).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Playlist already exists");
        }
        else {
            return playlistMapper.toDto(playlistRepository.save(playlistMapper.toEntity(playlist)));
        }
    }

    @Override
    @Transactional
    public List<PlaylistEntity> getAllPlaylists() {
        return playlistRepository.findAll();
    }

    @Override
    @Transactional
    public PlaylistEntity getPlaylist(String name) {

        if (name == null || name.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Playlist name cannot be null or empty");
        }
        return playlistRepository.findByName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Playlist not found"));
    }

    @Override
    @Transactional
    public void deletePlaylist(String name) {

        if (name == null || name.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Playlist name cannot be null or empty");
        }

        if (playlistRepository.findByName(name).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Playlist doesn't exists");
        }

        else {
            playlistRepository.deleteByName(name);
        }

    }

}
