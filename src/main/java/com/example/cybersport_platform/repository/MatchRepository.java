package com.example.cybersport_platform.repository;

import com.example.cybersport_platform.model.Match;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findByTournamentId(Long tournamentId);

    @Query("""
            select m from Match m
            join m.tournament t
            join t.game g
            where lower(g.name) like :gameNamePattern
            and lower(t.name) like :tournamentNamePattern
            and m.playedAt >= :playedFrom
            and m.playedAt <= :playedTo
            """)
    Page<Match> findByFiltersJpql(
            @Param("gameNamePattern") String gameNamePattern,
            @Param("tournamentNamePattern") String tournamentNamePattern,
            @Param("playedFrom") LocalDateTime playedFrom,
            @Param("playedTo") LocalDateTime playedTo,
            Pageable pageable
    );

    @Query(
            value = """
            select m.* from matches m
            join tournaments t on t.id = m.tournament_id
            join games g on g.id = t.game_id
            where lower(g.name) like :gameNamePattern
            and lower(t.name) like :tournamentNamePattern
            and m.playedat >= :playedFrom
            and m.playedat <= :playedTo
            """,
            countQuery = """
            select count(*) from matches m
            join tournaments t on t.id = m.tournament_id
            join games g on g.id = t.game_id
            where lower(g.name) like :gameNamePattern
            and lower(t.name) like :tournamentNamePattern
            and m.playedat >= :playedFrom
            and m.playedat <= :playedTo
            """,
            nativeQuery = true
    )
    Page<Match> findByFiltersNative(
            @Param("gameNamePattern") String gameNamePattern,
            @Param("tournamentNamePattern") String tournamentNamePattern,
            @Param("playedFrom") LocalDateTime playedFrom,
            @Param("playedTo") LocalDateTime playedTo,
            Pageable pageable
    );

    void deleteByTeam1IdOrTeam2Id(Long team1Id, Long team2Id);

    void deleteByTournamentIdIn(Collection<Long> tournamentIds);

    void deleteByTeam1IdInOrTeam2IdIn(Collection<Long> team1Ids, Collection<Long> team2Ids);
}
