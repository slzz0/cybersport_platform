package com.example.cybersport_platform.service.impl;

import com.example.cybersport_platform.cache.MatchSearchIndex;
import com.example.cybersport_platform.dto.request.PlayerRequest;
import com.example.cybersport_platform.exception.NotFoundException;
import com.example.cybersport_platform.model.Game;
import com.example.cybersport_platform.model.Team;
import com.example.cybersport_platform.repository.GameRepository;
import com.example.cybersport_platform.repository.PlayerRepository;
import com.example.cybersport_platform.repository.TeamRepository;
import com.example.cybersport_platform.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class PlayerBulkTransactionIntegrationTest {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private GameRepository gameRepository;

    @MockitoBean
    private MatchSearchIndex matchSearchIndex;

    private Team savedTeam;

    @BeforeEach
    void setUp() {
        playerRepository.deleteAll();
        teamRepository.deleteAll();
        gameRepository.deleteAll();

        Game game = new Game();
        game.setName("Counter-Strike 2");
        game.setDescription("Tactical shooter");
        Game savedGame = gameRepository.save(game);

        Team team = new Team();
        team.setName("Team Spirit");
        team.setGame(savedGame);
        savedTeam = teamRepository.save(team);
    }

    @Test
    void createBulkTransactionalShouldRollbackWholeBatchWhenOneRequestFails() {
        List<PlayerRequest> requests = List.of(
                new PlayerRequest("donk", 3200, savedTeam.getId()),
                new PlayerRequest("broken", 1500, 999_999L)
        );

        assertThatThrownBy(() -> playerService.createBulkTransactional(requests))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Team not found: 999999");

        assertThat(playerRepository.count()).isZero();
    }

    @Test
    void createBulkNonTransactionalShouldKeepAlreadySavedPlayersWhenOneRequestFails() {
        List<PlayerRequest> requests = List.of(
                new PlayerRequest("donk", 3200, savedTeam.getId()),
                new PlayerRequest("broken", 1500, 999_999L)
        );

        assertThatThrownBy(() -> playerService.createBulkNonTransactional(requests))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Team not found: 999999");

        assertThat(playerRepository.count()).isEqualTo(1);
        assertThat(playerRepository.findAll().getFirst().getNickname()).isEqualTo("donk");
    }
}
