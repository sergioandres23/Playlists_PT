package com.sergio.playlists.playlistsms.mappers;

import com.sergio.playlists.playlistsms.models.dto.SongDTO;
import com.sergio.playlists.playlistsms.models.entities.SongEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SongMapper {

    SongDTO toDto(SongEntity entity);

    SongEntity toEntity(SongDTO dto);

    List<SongDTO> toDtoList(List<SongEntity> entities);

    List<SongEntity> toEntityList(List<SongDTO> dtoList);
}
