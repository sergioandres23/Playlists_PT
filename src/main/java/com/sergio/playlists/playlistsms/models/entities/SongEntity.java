package com.sergio.playlists.playlistsms.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class SongEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String artist;
    private String album;
    @Column(name = "release_year")
    private Integer year;
    private String genre;
}
