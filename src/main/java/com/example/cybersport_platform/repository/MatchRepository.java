package com.example.cybersport_platform.repository;

import com.example.cybersport_platform.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findByTournamentId(Long tournamentId);

    void deleteByTeam1IdOrTeam2Id(Long team1Id, Long team2Id);

    void deleteByTournamentIdIn(Collection<Long> tournamentIds);

    void deleteByTeam1IdInOrTeam2IdIn(Collection<Long> team1Ids, Collection<Long> team2Ids);
}
