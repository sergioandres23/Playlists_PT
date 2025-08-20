package com.sergio.playlists.playlistsms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sergio.playlists.playlistsms.config.SecurityConfig;
import com.sergio.playlists.playlistsms.mappers.PlaylistMapper;
import com.sergio.playlists.playlistsms.models.dto.PlaylistDTO;
import com.sergio.playlists.playlistsms.models.dto.SongDTO;
import com.sergio.playlists.playlistsms.models.entities.PlaylistEntity;
import com.sergio.playlists.playlistsms.service.PlaylistsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PlaylistController.class)
@Import(SecurityConfig.class) // Importa tu configuración de seguridad
class PlaylistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Para convertir objetos a JSON

    @MockBean
    private PlaylistsService playlistsService;

    @MockBean
    private PlaylistMapper playlistMapper;

    private PlaylistDTO playlistDTO;
    private PlaylistEntity playlistEntity;

    @BeforeEach
    void setUp() {
        // Inicializa un SongDTO
        SongDTO songDTO1 = new SongDTO("One", "Metallica", "And Justice for All", 1988, "Metal");
        SongDTO songDTO2 = new SongDTO("Yellow", "Coldplay", "Parachutes", 2000, "Pop Rock");

        // Inicializa un PlaylistDTO
        playlistDTO = new PlaylistDTO("Lista 1", "Lista de canciones de Spotify", Arrays.asList(songDTO1, songDTO2));

        // Inicializa un PlaylistEntity (simulando una entidad persistida)
        playlistEntity = new PlaylistEntity();
        playlistEntity.setId(1L);
        playlistEntity.setName("Lista 1");
        playlistEntity.setDescription("Lista de canciones de Spotify");
        // Para simplificar, no mapeamos las canciones completas a la entidad aquí,
        // ya que la lógica de mapeo se prueba en los mappers y el servicio.
        // Solo nos importa que la entidad se devuelva correctamente.
    }

    @Test
    @DisplayName("POST /lists - Crear playlist exitosamente como ADMIN")
    @WithMockUser(roles = "ADMIN") // Simula un usuario con rol ADMIN
    void createPlaylist_ShouldReturnCreated_WhenAdmin() throws Exception {
        when(playlistsService.createPlaylist(any(PlaylistDTO.class))).thenReturn(playlistDTO);

        mockMvc.perform(post("/lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playlistDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(playlistDTO.getName()));

        verify(playlistsService, times(1)).createPlaylist(any(PlaylistDTO.class));
    }

    @Test
    @DisplayName("POST /lists - Retornar 400 Bad Request cuando el DTO es inválido")
    @WithMockUser(roles = "ADMIN")
    void createPlaylist_ShouldReturnBadRequest_WhenInvalidDto() throws Exception {
        PlaylistDTO invalidPlaylistDTO = new PlaylistDTO(null, "Descripción", Collections.emptyList()); // Nombre nulo

        mockMvc.perform(post("/lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPlaylistDTO)))
                .andExpect(status().isBadRequest());

        verify(playlistsService, never()).createPlaylist(any(PlaylistDTO.class));
    }

    @Test
    @DisplayName("POST /lists - Retornar 409 Conflict cuando la playlist ya existe")
    @WithMockUser(roles = "ADMIN")
    void createPlaylist_ShouldReturnConflict_WhenPlaylistAlreadyExists() throws Exception {
        when(playlistsService.createPlaylist(any(PlaylistDTO.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Playlist already exists"));

        mockMvc.perform(post("/lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playlistDTO)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Playlist already exists")); // Verifica el mensaje de la excepción

        verify(playlistsService, times(1)).createPlaylist(any(PlaylistDTO.class));
    }

    @Test
    @DisplayName("POST /lists - Retornar 403 Forbidden cuando el usuario no es ADMIN")
    @WithMockUser(roles = "USER") // Simula un usuario con rol USER
    void createPlaylist_ShouldReturnForbidden_WhenNotAdmin() throws Exception {
        mockMvc.perform(post("/lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playlistDTO)))
                .andExpect(status().isForbidden()); // Debería ser 403 Forbidden

        verify(playlistsService, never()).createPlaylist(any(PlaylistDTO.class));
    }

    @Test
    @DisplayName("GET /lists - Obtener todas las playlists exitosamente (permitAll)")
    void getAllPlaylists_ShouldReturnOk() throws Exception {
        List<PlaylistEntity> entities = Collections.singletonList(playlistEntity);
        List<PlaylistDTO> dtos = Collections.singletonList(playlistDTO);

        when(playlistsService.getAllPlaylists()).thenReturn(entities);
        when(playlistMapper.toDtoList(entities)).thenReturn(dtos);

        mockMvc.perform(get("/lists")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(playlistDTO.getName()));

        verify(playlistsService, times(1)).getAllPlaylists();
        verify(playlistMapper, times(1)).toDtoList(entities);
    }

    @Test
    @DisplayName("GET /lists/{name} - Obtener playlist por nombre exitosamente (permitAll)")
    void getPlaylistByName_ShouldReturnOk() throws Exception {
        when(playlistsService.getPlaylist(anyString())).thenReturn(playlistEntity);
        when(playlistMapper.toDto(playlistEntity)).thenReturn(playlistDTO);

        mockMvc.perform(get("/lists/{name}", "Lista 1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(playlistDTO.getName()));

        verify(playlistsService, times(1)).getPlaylist("Lista 1");
        verify(playlistMapper, times(1)).toDto(playlistEntity);
    }

    @Test
    @DisplayName("GET /lists/{name} - Retornar 404 Not Found cuando la playlist no existe")
    void getPlaylistByName_ShouldReturnNotFound_WhenNotFound() throws Exception {
        when(playlistsService.getPlaylist(anyString()))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Playlist not found"));

        mockMvc.perform(get("/lists/{name}", "NonExistentList")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Playlist not found"));

        verify(playlistsService, times(1)).getPlaylist("NonExistentList");
        verify(playlistMapper, never()).toDto(any(PlaylistEntity.class));
    }

    @Test
    @DisplayName("DELETE /lists/{name} - Eliminar playlist exitosamente como ADMIN")
    @WithMockUser(roles = "ADMIN")
    void deletePlaylist_ShouldReturnNoContent_WhenAdmin() throws Exception {
        doNothing().when(playlistsService).deletePlaylist(anyString());

        mockMvc.perform(delete("/lists/{name}", "Lista 1"))
                .andExpect(status().isNoContent());

        verify(playlistsService, times(1)).deletePlaylist("Lista 1");
    }

    @Test
    @DisplayName("DELETE /lists/{name} - Retornar 400 Bad Request cuando el nombre es nulo/vacío")
    @WithMockUser(roles = "ADMIN")
    void deletePlaylist_ShouldReturnBadRequest_WhenNameIsBlank() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Playlist name cannot be null or empty"))
                .when(playlistsService).deletePlaylist(anyString());

        mockMvc.perform(delete("/lists/{name}", " ")) // Nombre vacío
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Playlist name cannot be null or empty"));

        verify(playlistsService, times(1)).deletePlaylist(" ");
    }

    @Test
    @DisplayName("DELETE /lists/{name} - Retornar 404 Not Found cuando la playlist no existe")
    @WithMockUser(roles = "ADMIN")
    void deletePlaylist_ShouldReturnNotFound_WhenNotFound() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Playlist doesn't exists"))
                .when(playlistsService).deletePlaylist(anyString());

        mockMvc.perform(delete("/lists/{name}", "NonExistentList"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Playlist doesn't exists"));

        verify(playlistsService, times(1)).deletePlaylist("NonExistentList");
    }

    @Test
    @DisplayName("DELETE /lists/{name} - Retornar 403 Forbidden cuando el usuario no es ADMIN")
    @WithMockUser(roles = "USER")
    void deletePlaylist_ShouldReturnForbidden_WhenNotAdmin() throws Exception {
        mockMvc.perform(delete("/lists/{name}", "Lista 1"))
                .andExpect(status().isForbidden());

        verify(playlistsService, never()).deletePlaylist(anyString());
    }
}
