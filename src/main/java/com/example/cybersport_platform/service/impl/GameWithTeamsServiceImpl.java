package com.example.cybersport_platform.service.impl;

import com.example.cybersport_platform.dto.request.GameWithTeamsRequest;
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

    @Override
    public void saveGameWithTeamsAndPlayersNonTransactional(GameWithTeamsRequest request) {
        doSaveGameWithTeamsAndPlayers(request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveGameWithTeamsAndPlayersTransactional(GameWithTeamsRequest request) {
        doSaveGameWithTeamsAndPlayers(request);
    }

    private void doSaveGameWithTeamsAndPlayers(GameWithTeamsRequest request) {
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
        }

        for (int i = 0; i < teams.size(); i++) {
            Team t = teams.get(i);
            Player player = new Player();
            player.setNickname("player_" + t.getName());
            player.setElo(1000);
            if (request.getTeamId() != null && i == 1) {
                Team teamForError = teamRepository.findById(request.getTeamId())
                        .orElseThrow(() -> new IllegalStateException(
                                "Team not found for id: " + request.getTeamId()));
                player.setTeam(teamForError);
            } else {
                player.setTeam(t);
            }
            playerRepository.save(player);
        }
    }
}
