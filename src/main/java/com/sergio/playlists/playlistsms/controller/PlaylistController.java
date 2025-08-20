package com.sergio.playlists.playlistsms.controller;

import com.sergio.playlists.playlistsms.mappers.PlaylistMapper;
import com.sergio.playlists.playlistsms.models.dto.PlaylistDTO;
import com.sergio.playlists.playlistsms.models.entities.PlaylistEntity;
import com.sergio.playlists.playlistsms.service.PlaylistsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lists")
public class PlaylistController {

    private final PlaylistsService playlistsService;
    private final PlaylistMapper playlistMapper;

    public PlaylistController(PlaylistsService playlistsService, PlaylistMapper playlistMapper) {
        this.playlistsService = playlistsService;
        this.playlistMapper = playlistMapper;
    }

    @PostMapping
    public ResponseEntity<PlaylistDTO> createPlaylist(@Valid @RequestBody PlaylistDTO dto) {
        PlaylistDTO saved = playlistsService.createPlaylist(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }


    @GetMapping
    public List<PlaylistDTO> getAllPlaylists() {
        return playlistMapper.toDtoList(playlistsService.getAllPlaylists());
    }

    @GetMapping("/{name}")
    public ResponseEntity<PlaylistDTO> getPlaylist(@PathVariable String name) {
        PlaylistEntity playlist = playlistsService.getPlaylist(name);
        return ResponseEntity.ok(playlistMapper.toDto(playlist));
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable String name) {
        playlistsService.deletePlaylist(name);
        return ResponseEntity.noContent().build();
    }
}
