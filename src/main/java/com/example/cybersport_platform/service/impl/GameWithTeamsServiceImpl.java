package com.example.cybersport_platform.service.impl;

import com.example.cybersport_platform.cache.MatchSearchIndex;
import com.example.cybersport_platform.dto.request.GameWithTeamsRequest;
import com.example.cybersport_platform.model.Game;
import com.example.cybersport_platform.model.Team;
import com.example.cybersport_platform.repository.GameRepository;
import com.example.cybersport_platform.repository.TeamRepository;
import com.example.cybersport_platform.service.GameWithTeamsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GameWithTeamsServiceImpl implements GameWithTeamsService {

    private final GameRepository gameRepository;
    private final TeamRepository teamRepository;
    private final MatchSearchIndex matchSearchIndex;

    @Override
    public void saveGameWithTeamsNonTransactional(GameWithTeamsRequest request) {
        doSaveGameWithTeams(request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveGameWithTeamsTransactional(GameWithTeamsRequest request) {
        doSaveGameWithTeams(request);
    }

    private void doSaveGameWithTeams(GameWithTeamsRequest request) {
        Game game = new Game();
        game.setName(request.getGameName());
        game.setDescription(request.getGameDescription());
        game = gameRepository.save(game);

        for (String teamName : request.getTeamNames()) {
            Team team = new Team();
            team.setName(teamName);
            team.setGame(game);
            teamRepository.save(team);
        }

        if (request.getTeamId() != null) {
            Long teamId = request.getTeamId();
            teamRepository.findById(teamId)
                    .orElseThrow(() -> new IllegalStateException("Team not found for id: " + teamId));
        }
        matchSearchIndex.invalidateAll();
    }
}
