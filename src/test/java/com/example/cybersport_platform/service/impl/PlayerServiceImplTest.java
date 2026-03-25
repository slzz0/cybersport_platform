package com.example.cybersport_platform.service.impl;

import com.example.cybersport_platform.cache.MatchSearchIndex;
import com.example.cybersport_platform.dto.request.PlayerRequest;
import com.example.cybersport_platform.dto.response.PlayerResponse;
import com.example.cybersport_platform.exception.NotFoundException;
import com.example.cybersport_platform.model.Player;
import com.example.cybersport_platform.model.Team;
import com.example.cybersport_platform.repository.GameRepository;
import com.example.cybersport_platform.repository.PlayerRepository;
import com.example.cybersport_platform.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerServiceImplTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private MatchSearchIndex matchSearchIndex;

    @InjectMocks
    private PlayerServiceImpl playerService;

    private Team team;

    @BeforeEach
    void setUp() {
        team = new Team();
        team.setId(1L);
        team.setName("Team Spirit");
    }

    @Test
    void createBulkTransactionalShouldSaveAllPlayersAndInvalidateCacheOnce() {
        PlayerRequest first = new PlayerRequest("donk", 3200, 1L);
        PlayerRequest second = new PlayerRequest("zont1x", 3000, 1L);

        when(teamRepository.findAllById(anyIterable())).thenReturn(List.of(team));
        when(playerRepository.save(any(Player.class))).thenAnswer(invocation -> {
            Player player = invocation.getArgument(0);
            player.setId(player.getNickname().equals("donk") ? 10L : 11L);
            return player;
        });

        List<PlayerResponse> result = playerService.createBulkTransactional(List.of(first, second));

        assertThat(result)
                .hasSize(2)
                .extracting(PlayerResponse::getNickname)
                .containsExactly("donk", "zont1x");
        assertThat(result)
                .extracting(PlayerResponse::getTeamId)
                .containsOnly(1L);
        verify(playerRepository, times(2)).save(any(Player.class));
        verify(playerRepository, never()).saveAndFlush(any(Player.class));
        verify(matchSearchIndex).invalidateAll();
    }

    @Test
    void createBulkNonTransactionalShouldThrowWhenTeamIsMissingAfterPartialSave() {
        PlayerRequest first = new PlayerRequest("donk", 3200, 1L);
        PlayerRequest second = new PlayerRequest("ghost", 2500, 999L);
        List<PlayerRequest> requests = List.of(first, second);

        when(teamRepository.findAllById(anyIterable())).thenReturn(List.of(team));
        when(playerRepository.saveAndFlush(any(Player.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertThatThrownBy(() -> playerService.createBulkNonTransactional(requests))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Team not found: 999");

        ArgumentCaptor<Player> playerCaptor = ArgumentCaptor.forClass(Player.class);
        verify(playerRepository).saveAndFlush(playerCaptor.capture());
        assertThat(playerCaptor.getValue().getNickname()).isEqualTo("donk");
        verify(matchSearchIndex, never()).invalidateAll();
    }

    @Test
    void createShouldResolveTeamViaOptionalAndReturnMappedResponse() {
        PlayerRequest request = new PlayerRequest("Collapse", 3000, 1L);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(playerRepository.save(any(Player.class))).thenAnswer(invocation -> {
            Player player = invocation.getArgument(0);
            player.setId(7L);
            return player;
        });

        PlayerResponse result = playerService.create(request);

        assertThat(result.getId()).isEqualTo(7L);
        assertThat(result.getNickname()).isEqualTo("Collapse");
        assertThat(result.getTeamId()).isEqualTo(1L);
        assertThat(result.getTeamName()).isEqualTo("Team Spirit");
        verify(matchSearchIndex).invalidateAll();
    }

    @Test
    void createShouldThrowWhenTeamMissing() {
        PlayerRequest request = new PlayerRequest("Collapse", 3000, 99L);

        when(teamRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> playerService.create(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Team not found: 99");
    }

    @Test
    void updateShouldSaveExistingPlayerWithResolvedTeam() {
        PlayerRequest request = new PlayerRequest("Yatoro", 3100, 1L);
        Player existing = new Player();
        existing.setId(4L);
        existing.setNickname("Old");
        existing.setElo(2500);

        when(playerRepository.findById(4L)).thenReturn(Optional.of(existing));
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(playerRepository.save(existing)).thenReturn(existing);

        PlayerResponse result = playerService.update(4L, request);

        assertThat(existing.getNickname()).isEqualTo("Yatoro");
        assertThat(existing.getElo()).isEqualTo(3100);
        assertThat(existing.getTeam()).isSameAs(team);
        assertThat(result.getTeamName()).isEqualTo("Team Spirit");
        verify(matchSearchIndex).invalidateAll();
    }

    @Test
    void updateShouldClearTeamWhenRequestHasNullTeamId() {
        PlayerRequest request = new PlayerRequest("Solo", 2800, null);
        Player existing = new Player();
        existing.setId(5L);
        existing.setTeam(team);

        when(playerRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(playerRepository.save(existing)).thenReturn(existing);

        PlayerResponse result = playerService.update(5L, request);

        assertThat(existing.getTeam()).isNull();
        assertThat(result.getTeamId()).isNull();
        verify(matchSearchIndex).invalidateAll();
        verify(teamRepository, never()).findById(any());
    }

    @Test
    void updateShouldThrowWhenPlayerMissing() {
        PlayerRequest request = new PlayerRequest("Yatoro", 3100, 1L);

        when(playerRepository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> playerService.update(404L, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Player not found: 404");
    }

    @Test
    void createBulkNonTransactionalShouldSaveAllPlayersWhenAllTeamsExist() {
        PlayerRequest first = new PlayerRequest("donk", 3200, 1L);
        PlayerRequest second = new PlayerRequest("zont1x", 3000, 1L);

        when(teamRepository.findAllById(anyIterable())).thenReturn(List.of(team));
        when(playerRepository.saveAndFlush(any(Player.class))).thenAnswer(invocation -> {
            Player player = invocation.getArgument(0);
            player.setId(player.getNickname().equals("donk") ? 21L : 22L);
            return player;
        });

        List<PlayerResponse> result = playerService.createBulkNonTransactional(List.of(first, second));

        assertThat(result).hasSize(2);
        assertThat(result).extracting(PlayerResponse::getNickname)
                .containsExactly("donk", "zont1x");
        verify(playerRepository, times(2)).saveAndFlush(any(Player.class));
        verify(matchSearchIndex).invalidateAll();
    }

    @Test
    void createBulkTransactionalShouldHandleDuplicateTeamsFromRepository() {
        PlayerRequest request = new PlayerRequest("donk", 3200, 1L);

        Team duplicateTeam = new Team();
        duplicateTeam.setId(1L);
        duplicateTeam.setName("Team Spirit Duplicate");

        when(teamRepository.findAllById(anyIterable())).thenReturn(List.of(team, duplicateTeam));
        when(playerRepository.save(any(Player.class))).thenAnswer(invocation -> {
            Player player = invocation.getArgument(0);
            player.setId(31L);
            return player;
        });

        List<PlayerResponse> result = playerService.createBulkTransactional(List.of(request));

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getTeamName()).isEqualTo("Team Spirit");
        verify(playerRepository).save(any(Player.class));
        verify(matchSearchIndex).invalidateAll();
    }

    @Test
    void createBulkTransactionalShouldNotQueryTeamsWhenRequestListIsEmpty() {
        List<PlayerResponse> result = playerService.createBulkTransactional(List.of());

        assertThat(result).isEmpty();
        verify(teamRepository, never()).findAllById(anyIterable());
        verify(playerRepository, never()).save(any(Player.class));
        verify(matchSearchIndex).invalidateAll();
    }

    @Test
    void createBulkTransactionalShouldThrowNotFoundWithoutQueryingTeamsWhenAllTeamIdsAreNull() {
        PlayerRequest request = new PlayerRequest("donk", 3200, null);

        assertThatThrownBy(() -> playerService.createBulkTransactional(List.of(request)))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Team not found: null");

        verify(teamRepository, never()).findAllById(anyIterable());
        verify(playerRepository, never()).save(any(Player.class));
        verify(matchSearchIndex, never()).invalidateAll();
    }

    @Test
    void getByIdShouldReturnMappedPlayer() {
        Player player = new Player();
        player.setId(6L);
        player.setNickname("Mira");
        player.setElo(2900);
        player.setTeam(team);

        when(playerRepository.findById(6L)).thenReturn(Optional.of(player));

        PlayerResponse result = playerService.getById(6L);

        assertThat(result.getId()).isEqualTo(6L);
        assertThat(result.getNickname()).isEqualTo("Mira");
        assertThat(result.getTeamName()).isEqualTo("Team Spirit");
    }

    @Test
    void getByIdShouldThrowWhenPlayerMissing() {
        when(playerRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> playerService.getById(10L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Player not found: 10");
    }

    @Test
    void getAllShouldReturnMappedPlayers() {
        Player player = new Player();
        player.setId(7L);
        player.setNickname("Miposhka");
        player.setElo(2700);
        player.setTeam(team);

        when(playerRepository.findAll()).thenReturn(List.of(player));

        List<PlayerResponse> result = playerService.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getNickname()).isEqualTo("Miposhka");
        assertThat(result.getFirst().getTeamId()).isEqualTo(1L);
    }

    @Test
    void getByTeamIdShouldReturnEmptyListWhenTeamIdIsNull() {
        assertThat(playerService.getByTeamId(null)).isEmpty();
        verify(teamRepository, never()).existsById(any());
    }

    @Test
    void getByTeamIdShouldThrowWhenTeamMissing() {
        when(teamRepository.existsById(77L)).thenReturn(false);

        assertThatThrownBy(() -> playerService.getByTeamId(77L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Team not found: 77");
    }

    @Test
    void getByTeamIdShouldReturnMappedPlayers() {
        Player player = new Player();
        player.setId(8L);
        player.setNickname("Larl");
        player.setElo(2600);
        player.setTeam(team);

        when(teamRepository.existsById(1L)).thenReturn(true);
        when(playerRepository.findByTeamId(1L)).thenReturn(List.of(player));

        List<PlayerResponse> result = playerService.getByTeamId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getNickname()).isEqualTo("Larl");
    }

    @Test
    void getByGameIdShouldReturnEmptyListWhenGameIdIsNull() {
        assertThat(playerService.getByGameId(null)).isEmpty();
        verify(gameRepository, never()).existsById(any());
    }

    @Test
    void getByGameIdShouldThrowWhenGameMissing() {
        when(gameRepository.existsById(88L)).thenReturn(false);

        assertThatThrownBy(() -> playerService.getByGameId(88L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Game not found: 88");
    }

    @Test
    void getByGameIdShouldReturnMappedPlayers() {
        Player player = new Player();
        player.setId(9L);
        player.setNickname("Sh1ro");
        player.setElo(3050);
        player.setTeam(team);

        when(gameRepository.existsById(5L)).thenReturn(true);
        when(playerRepository.findByTeamGameIdWithTeam(5L)).thenReturn(List.of(player));

        List<PlayerResponse> result = playerService.getByGameId(5L);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getNickname()).isEqualTo("Sh1ro");
    }

    @Test
    void searchByNicknameShouldReturnEmptyListWhenNicknameBlank() {
        assertThat(playerService.searchByNickname("   ")).isEmpty();
        verify(playerRepository, never()).findByNicknameContainingIgnoreCase(any());
    }

    @Test
    void searchByNicknameShouldReturnEmptyListWhenNicknameIsNull() {
        assertThat(playerService.searchByNickname(null)).isEmpty();
        verify(playerRepository, never()).findByNicknameContainingIgnoreCase(any());
    }

    @Test
    void searchByNicknameShouldReturnMatchedPlayers() {
        Player player = new Player();
        player.setId(10L);
        player.setNickname("donk");
        player.setElo(3200);
        player.setTeam(team);

        when(playerRepository.findByNicknameContainingIgnoreCase("don")).thenReturn(List.of(player));

        List<PlayerResponse> result = playerService.searchByNickname("don");

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getNickname()).isEqualTo("donk");
    }

    @Test
    void deleteShouldDeletePlayerAndInvalidateCache() {
        when(playerRepository.existsById(3L)).thenReturn(true);
        doNothing().when(playerRepository).deleteById(3L);

        playerService.delete(3L);

        verify(playerRepository).deleteById(3L);
        verify(matchSearchIndex).invalidateAll();
    }

    @Test
    void deleteShouldThrowWhenPlayerMissing() {
        when(playerRepository.existsById(12L)).thenReturn(false);

        assertThatThrownBy(() -> playerService.delete(12L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Player not found: 12");

        verify(playerRepository, never()).deleteById(12L);
    }
}
