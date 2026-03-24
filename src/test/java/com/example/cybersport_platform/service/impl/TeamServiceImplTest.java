package com.example.cybersport_platform.service.impl;

import com.example.cybersport_platform.cache.MatchSearchIndex;
import com.example.cybersport_platform.dto.request.TeamRequest;
import com.example.cybersport_platform.dto.response.TeamResponse;
import com.example.cybersport_platform.exception.NotFoundException;
import com.example.cybersport_platform.model.Game;
import com.example.cybersport_platform.model.Team;
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
}
