package com.example.cybersport_platform.repository;

import com.example.cybersport_platform.model.Player;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PlayerRepository {

    private final List<Player> players = new ArrayList<>();

    @PostConstruct
    public void init() {
        players.add(new Player(1L, "s1mple", "BC.GAME", "CS2", 3500));
        players.add(new Player(2L, "slzzxd", "Greats", "CS2", 2300));
        players.add(new Player(3L, "donk", "Team Spirit", "CS2", 5000));
        players.add(new Player(4L, "m0NESY", "Falcons", "CS2", 4800));
    }

    public List<Player> findAll() {
        return new ArrayList<>(players);
    }

    public Optional<Player> findById(Long id) {
        return players.stream()
                .filter(player -> player.getId().equals(id))
                .findFirst();
    }

    public List<Player> findByNicknameContaining(String nickname) {
        return players.stream()
                .filter(player -> player.getNickname()
                        .toLowerCase()
                        .contains(nickname.toLowerCase()))
                .toList();
    }

    public List<Player> findByTeam(String team) {
        return players.stream()
                .filter(player -> player.getTeam()
                        .equalsIgnoreCase(team))
                .toList();
    }

    public List<Player> findByGame(String game) {
        return players.stream()
                .filter(player -> player.getGame()
                        .equalsIgnoreCase(game))
                .toList();
    }
}
