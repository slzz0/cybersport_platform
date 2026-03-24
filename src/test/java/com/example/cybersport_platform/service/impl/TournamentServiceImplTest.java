package com.example.cybersport_platform.service.impl;

import com.example.cybersport_platform.cache.MatchSearchIndex;
import com.example.cybersport_platform.dto.request.TournamentRequest;
import com.example.cybersport_platform.dto.response.TournamentResponse;
import com.example.cybersport_platform.exception.NotFoundException;
import com.example.cybersport_platform.model.Game;
import com.example.cybersport_platform.model.Team;
import com.example.cybersport_platform.model.Tournament;
import com.example.cybersport_platform.repository.GameRepository;
import com.example.cybersport_platform.repository.TournamentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TournamentServiceImplTest {

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private MatchSearchIndex matchSearchIndex;

    @InjectMocks
    private TournamentServiceImpl tournamentService;

    private TournamentRequest request(Long gameId) {
        return new TournamentRequest(
                "The International",
                LocalDate.of(2026, 8, 15),
                LocalDate.of(2026, 8, 25),
                "$1,000,000",
                gameId
        );
    }

    @Test
    void createShouldPersistTournamentWithGame() {
        Game game = new Game();
        game.setId(1L);
        game.setName("Dota 2");

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(tournamentRepository.save(any(Tournament.class))).thenAnswer(invocation -> {
            Tournament tournament = invocation.getArgument(0);
            tournament.setId(5L);
            return tournament;
        });

        TournamentResponse result = tournamentService.create(request(1L));

        assertThat(result.getId()).isEqualTo(5L);
        assertThat(result.getGameName()).isEqualTo("Dota 2");
        verify(matchSearchIndex).invalidateAll();
    }

    @Test
    void createShouldPersistTournamentWithoutGame() {
        when(tournamentRepository.save(any(Tournament.class))).thenAnswer(invocation -> {
            Tournament tournament = invocation.getArgument(0);
            tournament.setId(6L);
            return tournament;
        });

        TournamentResponse result = tournamentService.create(request(null));

        assertThat(result.getId()).isEqualTo(6L);
        assertThat(result.getGameId()).isNull();
        verify(gameRepository, never()).findById(any());
    }

    @Test
    void createShouldThrowWhenGameMissing() {
        when(gameRepository.findById(7L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tournamentService.create(request(7L)))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Game not found: 7");
    }

    @Test
    void updateShouldModifyTournamentAndGame() {
        Game game = new Game();
        game.setId(2L);
        game.setName("CS2");

        Tournament tournament = new Tournament();
        tournament.setId(8L);

        when(tournamentRepository.findById(8L)).thenReturn(Optional.of(tournament));
        when(gameRepository.findById(2L)).thenReturn(Optional.of(game));
        when(tournamentRepository.save(tournament)).thenReturn(tournament);

        TournamentResponse result = tournamentService.update(8L, request(2L));

        assertThat(tournament.getGame()).isSameAs(game);
        assertThat(result.getGameName()).isEqualTo("CS2");
        verify(matchSearchIndex).invalidateAll();
    }

    @Test
    void updateShouldClearGameWhenGameIdNull() {
        Tournament tournament = new Tournament();
        tournament.setId(9L);
        tournament.setGame(new Game());

        when(tournamentRepository.findById(9L)).thenReturn(Optional.of(tournament));
        when(tournamentRepository.save(tournament)).thenReturn(tournament);

        TournamentResponse result = tournamentService.update(9L, request(null));

        assertThat(tournament.getGame()).isNull();
        assertThat(result.getGameId()).isNull();
    }

    @Test
    void updateShouldThrowWhenTournamentMissing() {
        when(tournamentRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tournamentService.update(10L, request(1L)))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Tournament not found: 10");
    }

    @Test
    void updateShouldThrowWhenGameMissing() {
        Tournament tournament = new Tournament();
        tournament.setId(11L);

        when(tournamentRepository.findById(11L)).thenReturn(Optional.of(tournament));
        when(gameRepository.findById(77L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tournamentService.update(11L, request(77L)))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Game not found: 77");
    }

    @Test
    void getByIdShouldReturnTournament() {
        Tournament tournament = new Tournament();
        tournament.setId(12L);
        tournament.setName("Major");

        when(tournamentRepository.findById(12L)).thenReturn(Optional.of(tournament));

        assertThat(tournamentService.getById(12L).getName()).isEqualTo("Major");
    }

    @Test
    void getByIdShouldThrowWhenMissing() {
        when(tournamentRepository.findById(13L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tournamentService.getById(13L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Tournament not found: 13");
    }

    @Test
    void getAllShouldMapTournaments() {
        Tournament tournament = new Tournament();
        tournament.setId(14L);
        tournament.setName("Blast");

        when(tournamentRepository.findAll()).thenReturn(List.of(tournament));

        assertThat(tournamentService.getAll()).hasSize(1);
    }

    @Test
    void getByGameIdShouldReturnEmptyWhenNull() {
        assertThat(tournamentService.getByGameId(null)).isEmpty();
        verify(gameRepository, never()).existsById(any());
    }

    @Test
    void getByGameIdShouldThrowWhenGameMissing() {
        when(gameRepository.existsById(15L)).thenReturn(false);

        assertThatThrownBy(() -> tournamentService.getByGameId(15L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Game not found: 15");
    }

    @Test
    void getByGameIdShouldReturnMappedTournaments() {
        Tournament tournament = new Tournament();
        tournament.setId(16L);
        tournament.setName("DreamLeague");

        when(gameRepository.existsById(2L)).thenReturn(true);
        when(tournamentRepository.findByGameId(2L)).thenReturn(List.of(tournament));

        assertThat(tournamentService.getByGameId(2L)).hasSize(1);
    }

    @Test
    void deleteShouldRemoveTournamentRelations() {
        Tournament tournament = new Tournament();
        tournament.setId(17L);
        Team team = new Team();
        team.setId(1L);
        tournament.getTeams().add(team);
        team.getTournaments().add(tournament);

        when(tournamentRepository.findById(17L)).thenReturn(Optional.of(tournament));

        tournamentService.delete(17L);

        assertThat(tournament.getTeams()).isEmpty();
        assertThat(team.getTournaments()).isEmpty();
        verify(tournamentRepository).delete(tournament);
        verify(matchSearchIndex).invalidateAll();
    }

    @Test
    void deleteShouldThrowWhenMissing() {
        when(tournamentRepository.findById(18L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tournamentService.delete(18L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Tournament not found: 18");
    }
}
