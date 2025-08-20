package com.sergio.playlists.playlistsms.models.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class SongDTO {

    @NotBlank
    private String title;
    @NotBlank
    private String artist;
    private String album;
    private Integer year;
    private String genre;

}
