package com.example.cybersport_platform.service.impl;

import com.example.cybersport_platform.cache.MatchSearchIndex;
import com.example.cybersport_platform.dto.request.TeamRequest;
import com.example.cybersport_platform.dto.response.TeamResponse;
import com.example.cybersport_platform.exception.NotFoundException;
import com.example.cybersport_platform.model.Game;
import com.example.cybersport_platform.model.Team;
import com.example.cybersport_platform.model.Tournament;
import com.example.cybersport_platform.repository.GameRepository;
import com.example.cybersport_platform.repository.MatchRepository;
import com.example.cybersport_platform.repository.TeamRepository;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamServiceImplTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private MatchSearchIndex matchSearchIndex;

    @InjectMocks
    private TeamServiceImpl teamService;

    @Test
    void createShouldPersistTeamWithGame() {
        Game game = new Game();
        game.setId(3L);
        game.setName("Counter-Strike 2");

        TeamRequest request = new TeamRequest("Natus Vincere", 3L);

        when(gameRepository.findById(3L)).thenReturn(Optional.of(game));
        when(teamRepository.save(any(Team.class))).thenAnswer(invocation -> {
            Team team = invocation.getArgument(0);
            team.setId(5L);
            return team;
        });

        TeamResponse result = teamService.create(request);

        ArgumentCaptor<Team> teamCaptor = ArgumentCaptor.forClass(Team.class);
        verify(teamRepository).save(teamCaptor.capture());
        assertThat(teamCaptor.getValue().getGame()).isSameAs(game);
        assertThat(result.getId()).isEqualTo(5L);
        assertThat(result.getGameId()).isEqualTo(3L);
        verify(matchSearchIndex).invalidateAll();
    }

    @Test
    void getByGameIdShouldThrowWhenGameDoesNotExist() {
        when(gameRepository.existsById(77L)).thenReturn(false);

        assertThatThrownBy(() -> teamService.getByGameId(77L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Game not found: 77");

        verify(teamRepository, never()).findByGameId(77L);
    }

    @Test
    void getAllShouldMapRepositoryResult() {
        Game game = new Game();
        game.setId(2L);
        game.setName("Dota 2");

        Team team = new Team();
        team.setId(8L);
        team.setName("Team Liquid");
        team.setGame(game);

        when(teamRepository.findAll()).thenReturn(List.of(team));

        List<TeamResponse> result = teamService.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getName()).isEqualTo("Team Liquid");
        assertThat(result.getFirst().getGameId()).isEqualTo(2L);
    }

    @Test
    void updateShouldPersistNewValues() {
        Game game = new Game();
        game.setId(4L);
        game.setName("Valorant");

        Team existing = new Team();
        existing.setId(6L);
        existing.setName("Old Team");

        TeamRequest request = new TeamRequest("Fnatic", 4L);

        when(teamRepository.findById(6L)).thenReturn(Optional.of(existing));
        when(gameRepository.findById(4L)).thenReturn(Optional.of(game));
        when(teamRepository.save(existing)).thenReturn(existing);

        TeamResponse result = teamService.update(6L, request);

        assertThat(existing.getName()).isEqualTo("Fnatic");
        assertThat(existing.getGame()).isSameAs(game);
        assertThat(result.getGameName()).isEqualTo("Valorant");
        verify(matchSearchIndex).invalidateAll();
    }

    @Test
    void getByIdShouldReturnMappedTeam() {
        Game game = new Game();
        game.setId(5L);
        game.setName("League of Legends");

        Team team = new Team();
        team.setId(9L);
        team.setName("T1");
        team.setGame(game);

        when(teamRepository.findById(9L)).thenReturn(Optional.of(team));

        TeamResponse result = teamService.getById(9L);

        assertThat(result.getId()).isEqualTo(9L);
        assertThat(result.getGameName()).isEqualTo("League of Legends");
    }

    @Test
    void getByIdShouldThrowWhenTeamMissing() {
        when(teamRepository.findById(15L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> teamService.getById(15L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Team not found: 15");
    }

    @Test
    void getByGameIdShouldReturnEmptyListWhenGameIdIsNull() {
        assertThat(teamService.getByGameId(null)).isEmpty();
        verify(gameRepository, never()).existsById(any());
    }

    @Test
    void getByGameIdShouldReturnMappedTeamsWhenGameExists() {
        Game game = new Game();
        game.setId(2L);
        game.setName("Dota 2");

        Team team = new Team();
        team.setId(10L);
        team.setName("BetBoom");
        team.setGame(game);

        when(gameRepository.existsById(2L)).thenReturn(true);
        when(teamRepository.findByGameId(2L)).thenReturn(List.of(team));

        List<TeamResponse> result = teamService.getByGameId(2L);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getName()).isEqualTo("BetBoom");
    }

    @Test
    void deleteShouldRemoveLinksMatchesAndTeam() {
        Tournament tournament = new Tournament();
        tournament.setId(3L);

        Team team = new Team();
        team.setId(11L);
        team.getTournaments().add(tournament);
        tournament.getTeams().add(team);

        when(teamRepository.findById(11L)).thenReturn(Optional.of(team));
        doNothing().when(matchRepository).deleteByTeam1IdOrTeam2Id(11L, 11L);
        doNothing().when(teamRepository).delete(team);

        teamService.delete(11L);

        assertThat(team.getTournaments()).isEmpty();
        assertThat(tournament.getTeams()).isEmpty();
        verify(matchRepository).deleteByTeam1IdOrTeam2Id(11L, 11L);
        verify(teamRepository).delete(team);
        verify(matchSearchIndex).invalidateAll();
    }

    @Test
    void deleteShouldThrowWhenTeamMissing() {
        when(teamRepository.findById(20L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> teamService.delete(20L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Team not found: 20");
    }
}
