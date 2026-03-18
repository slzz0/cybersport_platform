package com.example.cybersport_platform.service.impl;

import com.example.cybersport_platform.cache.MatchSearchCacheKey;
import com.example.cybersport_platform.cache.MatchSearchIndex;
import com.example.cybersport_platform.dto.request.MatchRequest;
import com.example.cybersport_platform.dto.response.MatchResponse;
import com.example.cybersport_platform.exception.NotFoundException;
import com.example.cybersport_platform.mapper.MatchMapper;
import com.example.cybersport_platform.model.Match;
import com.example.cybersport_platform.repository.MatchRepository;
import com.example.cybersport_platform.repository.TeamRepository;
import com.example.cybersport_platform.repository.TournamentRepository;
import com.example.cybersport_platform.service.MatchSearchQueryType;
import com.example.cybersport_platform.service.MatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private static final String MATCH_NOT_FOUND_MESSAGE = "Match not found: ";
    private static final String TEAM_NOT_FOUND_MESSAGE = "Team not found: ";
    private static final String TOURNAMENT_NOT_FOUND_MESSAGE = "Tournament not found: ";
    private static final LocalDateTime DEFAULT_PLAYED_FROM = LocalDateTime.of(1970, 1, 1, 0, 0);
    private static final LocalDateTime DEFAULT_PLAYED_TO = LocalDateTime.of(2999, 12, 31, 23, 59, 59);
    private static final String CACHE_HIT_LOG_MESSAGE =
            "Match search cache hit: queryType={}, gameName={}, tournamentName={}, playedFrom={}, playedTo={}, "
            + "pageable={}";
    private static final String CACHE_MISS_LOG_MESSAGE =
            "Match search cache miss: queryType={}, gameName={}, tournamentName={}, playedFrom={}, playedTo={}, "
            + "pageable={}";
    private static final String CACHE_STORE_LOG_MESSAGE =
            "Match search cache store: queryType={}, gameName={}, tournamentName={}, playedFrom={}, playedTo={}, "
            + "totalElements={}";

    private final MatchRepository matchRepository;
    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;
    private final MatchSearchIndex matchSearchIndex;

    @Override
    @Transactional
    public MatchResponse create(MatchRequest request) {
        Match match = new Match();
        match.setScoreTeam1(request.getScoreTeam1());
        match.setScoreTeam2(request.getScoreTeam2());
        match.setPlayedAt(request.getPlayedAt());
        if (request.getTournamentId() != null) {
            Long tournamentId = request.getTournamentId();
            match.setTournament(tournamentRepository.findById(tournamentId)
                    .orElseThrow(() -> new NotFoundException(TOURNAMENT_NOT_FOUND_MESSAGE + tournamentId)));
        }
        if (request.getTeam1Id() != null) {
            Long team1Id = request.getTeam1Id();
            match.setTeam1(teamRepository.findById(team1Id)
                    .orElseThrow(() -> new NotFoundException(TEAM_NOT_FOUND_MESSAGE + team1Id)));
        }
        if (request.getTeam2Id() != null) {
            Long team2Id = request.getTeam2Id();
            match.setTeam2(teamRepository.findById(team2Id)
                    .orElseThrow(() -> new NotFoundException(TEAM_NOT_FOUND_MESSAGE + team2Id)));
        }
        MatchResponse response = MatchMapper.toResponse(matchRepository.save(match));
        invalidateSearchIndex("match created");
        return response;
    }

    @Override
    @Transactional
    public MatchResponse update(Long id, MatchRequest request) {
        Match existing = matchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MATCH_NOT_FOUND_MESSAGE + id));
        existing.setScoreTeam1(request.getScoreTeam1());
        existing.setScoreTeam2(request.getScoreTeam2());
        existing.setPlayedAt(request.getPlayedAt());
        if (request.getTournamentId() != null) {
            Long tournamentId = request.getTournamentId();
            existing.setTournament(tournamentRepository.findById(tournamentId)
                    .orElseThrow(() -> new NotFoundException(TOURNAMENT_NOT_FOUND_MESSAGE + tournamentId)));
        } else {
            existing.setTournament(null);
        }
        if (request.getTeam1Id() != null) {
            Long team1Id = request.getTeam1Id();
            existing.setTeam1(teamRepository.findById(team1Id)
                    .orElseThrow(() -> new NotFoundException(TEAM_NOT_FOUND_MESSAGE + team1Id)));
        } else {
            existing.setTeam1(null);
        }
        if (request.getTeam2Id() != null) {
            Long team2Id = request.getTeam2Id();
            existing.setTeam2(teamRepository.findById(team2Id)
                    .orElseThrow(() -> new NotFoundException(TEAM_NOT_FOUND_MESSAGE + team2Id)));
        } else {
            existing.setTeam2(null);
        }
        MatchResponse response = MatchMapper.toResponse(matchRepository.save(existing));
        invalidateSearchIndex("match updated");
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public MatchResponse getById(Long id) {
        return matchRepository.findById(id)
                .map(MatchMapper::toResponse)
                .orElseThrow(() -> new NotFoundException(MATCH_NOT_FOUND_MESSAGE + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MatchResponse> getAll() {
        return matchRepository.findAll().stream()
                .map(MatchMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MatchResponse> getByTournamentId(Long tournamentId) {
        if (tournamentId == null) {
            return List.of();
        }
        if (!tournamentRepository.existsById(tournamentId)) {
            throw new NotFoundException(TOURNAMENT_NOT_FOUND_MESSAGE + tournamentId);
        }
        return matchRepository.findByTournamentId(tournamentId).stream()
                .map(MatchMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MatchResponse> getAllPaged(Pageable pageable) {
        return matchRepository.findAll(pageable).map(MatchMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MatchResponse> searchByFiltersJpql(
            String gameName,
            String tournamentName,
            LocalDateTime playedFrom,
            LocalDateTime playedTo,
            Pageable pageable
    ) {
        return findFilteredMatches(
                gameName,
                tournamentName,
                playedFrom,
                playedTo,
                pageable,
                MatchSearchQueryType.JPQL
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MatchResponse> searchByFiltersNative(
            String gameName,
            String tournamentName,
            LocalDateTime playedFrom,
            LocalDateTime playedTo,
            Pageable pageable
    ) {
        return findFilteredMatches(
                gameName,
                tournamentName,
                playedFrom,
                playedTo,
                pageable,
                MatchSearchQueryType.NATIVE
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MatchResponse> getByFilters(
            String gameName,
            String tournamentName,
            LocalDateTime playedFrom,
            LocalDateTime playedTo,
            Pageable pageable,
            MatchSearchQueryType queryType
    ) {
        return findFilteredMatches(
                gameName,
                tournamentName,
                playedFrom,
                playedTo,
                pageable,
                queryType
        );
    }

    private Page<MatchResponse> findFilteredMatches(
            String gameName,
            String tournamentName,
            LocalDateTime playedFrom,
            LocalDateTime playedTo,
            Pageable pageable,
            MatchSearchQueryType queryType
    ) {
        String gameNamePattern = toLikePattern(gameName);
        String tournamentNamePattern = toLikePattern(tournamentName);
        LocalDateTime effectivePlayedFrom = playedFrom == null ? DEFAULT_PLAYED_FROM : playedFrom;
        LocalDateTime effectivePlayedTo = playedTo == null ? DEFAULT_PLAYED_TO : playedTo;
        MatchSearchCacheKey key = new MatchSearchCacheKey(
                gameName,
                tournamentName,
                playedFrom,
                playedTo,
                pageable,
                queryType
        );
        return matchSearchIndex.get(key)
                .map(page -> {
                    log.debug(CACHE_HIT_LOG_MESSAGE, queryType, gameName, tournamentName, playedFrom, playedTo,
                            pageable);
                    return page;
                })
                .orElseGet(() -> {
                    log.debug(CACHE_MISS_LOG_MESSAGE, queryType, gameName, tournamentName, playedFrom, playedTo,
                            pageable);
                    Page<MatchResponse> page = getMatchesPageByQueryType(
                            gameNamePattern,
                            tournamentNamePattern,
                            effectivePlayedFrom,
                            effectivePlayedTo,
                            pageable,
                            queryType
                    ).map(MatchMapper::toResponse);
                    matchSearchIndex.put(key, page);
                    log.debug(CACHE_STORE_LOG_MESSAGE, queryType, gameName, tournamentName, playedFrom, playedTo,
                            page.getTotalElements());
                    return page;
                });
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!matchRepository.existsById(id)) {
            throw new NotFoundException(MATCH_NOT_FOUND_MESSAGE + id);
        }
        matchRepository.deleteById(id);
        invalidateSearchIndex("match deleted");
    }

    private Page<Match> getMatchesPageByQueryType(
            String gameNamePattern,
            String tournamentNamePattern,
            LocalDateTime playedFrom,
            LocalDateTime playedTo,
            Pageable pageable,
            MatchSearchQueryType queryType
    ) {
        if (queryType == MatchSearchQueryType.NATIVE) {
            return matchRepository.findByFiltersNative(
                    gameNamePattern,
                    tournamentNamePattern,
                    playedFrom,
                    playedTo,
                    pageable
            );
        }
        return matchRepository.findByFiltersJpql(
                gameNamePattern,
                tournamentNamePattern,
                playedFrom,
                playedTo,
                pageable
        );
    }

    private String toLikePattern(String value) {
        if (value == null || value.isBlank()) {
            return "%";
        }
        return "%" + value.toLowerCase() + "%";
    }

    private void invalidateSearchIndex(String reason) {
        matchSearchIndex.invalidateAll();
        log.debug("Match search cache invalidated: {}", reason);
    }
}
