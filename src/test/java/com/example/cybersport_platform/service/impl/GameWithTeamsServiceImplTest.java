package com.example.cybersport_platform.service.impl;

import com.example.cybersport_platform.cache.MatchSearchIndex;
import com.example.cybersport_platform.dto.request.GameWithTeamsRequest;
import com.example.cybersport_platform.model.Game;
import com.example.cybersport_platform.model.Team;
import com.example.cybersport_platform.repository.GameRepository;
import com.example.cybersport_platform.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameWithTeamsServiceImplTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private MatchSearchIndex matchSearchIndex;

    @InjectMocks
    private GameWithTeamsServiceImpl service;

    private GameWithTeamsRequest request(Long teamId) {
        return new GameWithTeamsRequest(
                "Counter-Strike 2",
                "Tactical shooter",
                List.of("NAVI", "G2"),
                teamId
        );
    }

    @Test
    void saveNonTransactionalShouldPersistGameAndTeamsAndInvalidateCache() {
        Game savedGame = new Game();
        savedGame.setId(1L);
        savedGame.setName("Counter-Strike 2");

        when(gameRepository.save(any(Game.class))).thenReturn(savedGame);
        when(teamRepository.save(any(Team.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.saveGameWithTeamsNonTransactional(request(null));

        verify(gameRepository).save(any(Game.class));
        verify(teamRepository, times(2)).save(any(Team.class));
        verify(teamRepository, never()).findById(any());
        verify(matchSearchIndex).invalidateAll();
    }

    @Test
    void saveTransactionalShouldValidateExistingTeamWhenTeamIdPresent() {
        Game savedGame = new Game();
        savedGame.setId(2L);

        Team existingTeam = new Team();
        existingTeam.setId(10L);

        when(gameRepository.save(any(Game.class))).thenReturn(savedGame);
        when(teamRepository.save(any(Team.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(teamRepository.findById(10L)).thenReturn(Optional.of(existingTeam));

        service.saveGameWithTeamsTransactional(request(10L));

        verify(teamRepository).findById(10L);
        verify(matchSearchIndex).invalidateAll();
    }

    @Test
    void saveTransactionalShouldThrowWhenLookupTeamMissing() {
        Game savedGame = new Game();
        savedGame.setId(3L);

        when(gameRepository.save(any(Game.class))).thenReturn(savedGame);
        when(teamRepository.save(any(Team.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(teamRepository.findById(55L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.saveGameWithTeamsTransactional(request(55L)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Team not found for id: 55");
    }
}
