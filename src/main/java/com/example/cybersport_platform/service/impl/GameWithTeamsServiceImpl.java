package com.example.cybersport_platform.service.impl;

import com.example.cybersport_platform.dto.request.GameWithTeamsRequest;
import com.example.cybersport_platform.exception.DemoSimulatedException;
import com.example.cybersport_platform.model.Game;
import com.example.cybersport_platform.model.Player;
import com.example.cybersport_platform.model.Team;
import com.example.cybersport_platform.repository.GameRepository;
import com.example.cybersport_platform.repository.PlayerRepository;
import com.example.cybersport_platform.repository.TeamRepository;
import com.example.cybersport_platform.service.GameWithTeamsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameWithTeamsServiceImpl implements GameWithTeamsService {

    private final GameRepository gameRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    private static final String ERROR_MSG = "Имитация ошибки после сохранения второй команды (демо транзакций)";

    @Override
    public void saveGameWithTeamsAndPlayersNonTransactional(GameWithTeamsRequest request) {
        // Без @Transactional: каждый save() выполняется в своей транзакции и сразу коммитится.
        // При исключении ниже в БД уже будут игра и две команды — частичное сохранение.
        Game game = new Game();
        game.setName(request.getGameName());
        game.setDescription(request.getGameDescription());
        game = gameRepository.save(game);

        List<Team> teams = new ArrayList<>();
        for (int i = 0; i < request.getTeamNames().size(); i++) {
            Team team = new Team();
            team.setName(request.getTeamNames().get(i));
            team.setGame(game);
            team = teamRepository.save(team);
            teams.add(team);

            if (request.isSimulateError() && i == 1) {
                throw new DemoSimulatedException(ERROR_MSG);
            }
        }

        for (int i = 0; i < teams.size(); i++) {
            Player player = new Player();
            player.setNickname("player_" + teams.get(i).getName());
            player.setElo(1000);
            player.setTeam(teams.get(i));
            playerRepository.save(player);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveGameWithTeamsAndPlayersTransactional(GameWithTeamsRequest request) {
        // С @Transactional: всё в одной транзакции. При любом исключении — полный откат.
        Game game = new Game();
        game.setName(request.getGameName());
        game.setDescription(request.getGameDescription());
        game = gameRepository.save(game);

        List<Team> teams = new ArrayList<>();
        for (int i = 0; i < request.getTeamNames().size(); i++) {
            Team team = new Team();
            team.setName(request.getTeamNames().get(i));
            team.setGame(game);
            team = teamRepository.save(team);
            teams.add(team);

            if (request.isSimulateError() && i == 1) {
                throw new DemoSimulatedException(ERROR_MSG);
            }
        }

        for (Team t : teams) {
            Player player = new Player();
            player.setNickname("player_" + t.getName());
            player.setElo(1000);
            player.setTeam(t);
            playerRepository.save(player);
        }
    }
}
