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

        when(teamRepository.findAllById(anyIterable())).thenReturn(List.of(team));
        when(playerRepository.saveAndFlush(any(Player.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertThatThrownBy(() -> playerService.createBulkNonTransactional(List.of(first, second)))
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
}
