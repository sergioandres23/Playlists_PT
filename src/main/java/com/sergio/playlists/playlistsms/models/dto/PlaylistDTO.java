package com.sergio.playlists.playlistsms.models.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PlaylistDTO {

    @NotBlank
    private String name;
    private String description;
    @Valid
    private List<SongDTO> songs;

}
