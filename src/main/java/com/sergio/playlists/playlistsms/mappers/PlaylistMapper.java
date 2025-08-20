package com.sergio.playlists.playlistsms.mappers;

import com.sergio.playlists.playlistsms.models.dto.PlaylistDTO;
import com.sergio.playlists.playlistsms.models.entities.PlaylistEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {SongMapper.class})
public interface PlaylistMapper {

    PlaylistDTO toDto(PlaylistEntity entity);

    PlaylistEntity toEntity(PlaylistDTO dto);

    List<PlaylistDTO> toDtoList(List<PlaylistEntity> entities);
}
