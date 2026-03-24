package com.example.cybersport_platform.service.impl;

import com.example.cybersport_platform.cache.MatchSearchIndex;
import com.example.cybersport_platform.dto.request.GameRequest;
import com.example.cybersport_platform.dto.response.GameResponse;
import com.example.cybersport_platform.exception.NotFoundException;
import com.example.cybersport_platform.model.Game;
import com.example.cybersport_platform.model.Team;
import com.example.cybersport_platform.model.Tournament;
import com.example.cybersport_platform.repository.GameRepository;
import com.example.cybersport_platform.repository.MatchRepository;
import com.example.cybersport_platform.repository.TeamRepository;
import com.example.cybersport_platform.repository.TournamentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceImplTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private MatchSearchIndex matchSearchIndex;

    @InjectMocks
    private GameServiceImpl gameService;

    @Test
    void createShouldPersistGame() {
        GameRequest request = new GameRequest("Dota 2", "MOBA");

        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> {
            Game game = invocation.getArgument(0);
            game.setId(1L);
            return game;
        });

        GameResponse result = gameService.create(request);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Dota 2");
        verify(matchSearchIndex).invalidateAll();
    }

    @Test
    void updateShouldModifyExistingGame() {
        Game existing = new Game();
        existing.setId(2L);
        existing.setName("Old");

        GameRequest request = new GameRequest("CS2", "Shooter");

        when(gameRepository.findById(2L)).thenReturn(Optional.of(existing));
        when(gameRepository.save(existing)).thenReturn(existing);

        GameResponse result = gameService.update(2L, request);

        assertThat(existing.getName()).isEqualTo("CS2");
        assertThat(existing.getDescription()).isEqualTo("Shooter");
        assertThat(result.getDescription()).isEqualTo("Shooter");
        verify(matchSearchIndex).invalidateAll();
    }

    @Test
    void updateShouldThrowWhenGameMissing() {
        when(gameRepository.findById(9L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gameService.update(9L, new GameRequest("A", "B")))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Game not found: 9");
    }

    @Test
    void getByIdShouldReturnMappedGame() {
        Game game = new Game();
        game.setId(3L);
        game.setName("Valorant");
        game.setDescription("FPS");

        when(gameRepository.findById(3L)).thenReturn(Optional.of(game));

        GameResponse result = gameService.getById(3L);

        assertThat(result.getName()).isEqualTo("Valorant");
    }

    @Test
    void getByIdShouldThrowWhenGameMissing() {
        when(gameRepository.findById(4L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gameService.getById(4L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Game not found: 4");
    }

    @Test
    void getAllShouldMapAllGames() {
        Game game = new Game();
        game.setId(5L);
        game.setName("LoL");
        game.setDescription("MOBA");

        when(gameRepository.findAll()).thenReturn(List.of(game));

        List<GameResponse> result = gameService.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getName()).isEqualTo("LoL");
    }

    @Test
    void deleteShouldRemoveGameRelationsAndInvalidateCache() {
        Game game = new Game();
        game.setId(6L);

        Team team = new Team();
        team.setId(10L);

        Tournament tournament = new Tournament();
        tournament.setId(20L);

        team.getTournaments().add(tournament);
        tournament.getTeams().add(team);

        when(gameRepository.findById(6L)).thenReturn(Optional.of(game));
        when(teamRepository.findByGameId(6L)).thenReturn(List.of(team));
        when(tournamentRepository.findByGameId(6L)).thenReturn(List.of(tournament));
        doNothing().when(matchRepository).deleteByTeam1IdInOrTeam2IdIn(any(), any());
        doNothing().when(matchRepository).deleteByTournamentIdIn(any());
        doNothing().when(tournamentRepository).deleteAll(List.of(tournament));
        doNothing().when(teamRepository).deleteAll(List.of(team));
        doNothing().when(gameRepository).delete(game);

        gameService.delete(6L);

        assertThat(team.getTournaments()).isEmpty();
        assertThat(tournament.getTeams()).isEmpty();
        verify(matchRepository).deleteByTeam1IdInOrTeam2IdIn(any(), any());
        verify(matchRepository).deleteByTournamentIdIn(any());
        verify(tournamentRepository).deleteAll(List.of(tournament));
        verify(teamRepository).deleteAll(List.of(team));
        verify(gameRepository).delete(game);
        verify(matchSearchIndex).invalidateAll();
    }

    @Test
    void deleteShouldSkipMatchDeletionWhenNoTeamsOrTournaments() {
        Game game = new Game();
        game.setId(7L);

        when(gameRepository.findById(7L)).thenReturn(Optional.of(game));
        when(teamRepository.findByGameId(7L)).thenReturn(List.of());
        when(tournamentRepository.findByGameId(7L)).thenReturn(List.of());

        gameService.delete(7L);

        verify(matchRepository, never()).deleteByTeam1IdInOrTeam2IdIn(any(), any());
        verify(matchRepository, never()).deleteByTournamentIdIn(any());
        verify(matchSearchIndex).invalidateAll();
    }

    @Test
    void deleteShouldThrowWhenGameMissing() {
        when(gameRepository.findById(8L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gameService.delete(8L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Game not found: 8");
    }
}
