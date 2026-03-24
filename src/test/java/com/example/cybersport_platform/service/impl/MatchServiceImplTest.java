package com.example.cybersport_platform.service.impl;

import com.example.cybersport_platform.cache.MatchSearchIndex;
import com.example.cybersport_platform.dto.request.MatchRequest;
import com.example.cybersport_platform.dto.response.MatchResponse;
import com.example.cybersport_platform.exception.NotFoundException;
import com.example.cybersport_platform.model.Match;
import com.example.cybersport_platform.model.Team;
import com.example.cybersport_platform.model.Tournament;
import com.example.cybersport_platform.repository.MatchRepository;
import com.example.cybersport_platform.repository.TeamRepository;
import com.example.cybersport_platform.repository.TournamentRepository;
import com.example.cybersport_platform.service.MatchSearchQueryType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MatchServiceImplTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private MatchSearchIndex matchSearchIndex;

    @InjectMocks
    private MatchServiceImpl matchService;

    private Tournament tournament;
    private Team team1;
    private Team team2;
    private MatchRequest request;

    @BeforeEach
    void setUp() {
        tournament = new Tournament();
        tournament.setId(1L);

        team1 = new Team();
        team1.setId(10L);

        team2 = new Team();
        team2.setId(11L);

        request = new MatchRequest(
                1L,
                10L,
                11L,
                2,
                1,
                LocalDateTime.of(2026, 3, 16, 14, 30)
        );
    }

    @Test
    void createShouldPersistMatchWithRelations() {
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team1));
        when(teamRepository.findById(11L)).thenReturn(Optional.of(team2));
        when(matchRepository.save(any(Match.class))).thenAnswer(invocation -> {
            Match match = invocation.getArgument(0);
            match.setId(5L);
            return match;
        });

        MatchResponse result = matchService.create(request);

        assertThat(result.getId()).isEqualTo(5L);
        assertThat(result.getTournamentId()).isEqualTo(1L);
        verify(matchSearchIndex).invalidateAll();
    }

    @Test
    void createShouldPersistMatchWithoutOptionalRelations() {
        MatchRequest nullableRelationsRequest = new MatchRequest(
                null,
                null,
                null,
                3,
                2,
                LocalDateTime.of(2026, 3, 20, 18, 0)
        );

        when(matchRepository.save(any(Match.class))).thenAnswer(invocation -> {
            Match match = invocation.getArgument(0);
            match.setId(6L);
            return match;
        });

        MatchResponse result = matchService.create(nullableRelationsRequest);

        assertThat(result.getId()).isEqualTo(6L);
        assertThat(result.getTournamentId()).isNull();
        assertThat(result.getTeam1Id()).isNull();
        assertThat(result.getTeam2Id()).isNull();
        verify(tournamentRepository, never()).findById(any());
        verify(teamRepository, never()).findById(any());
        verify(matchSearchIndex).invalidateAll();
    }

    @Test
    void createShouldThrowWhenTournamentMissing() {
        when(tournamentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> matchService.create(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Tournament not found: 1");
    }

    @Test
    void createShouldThrowWhenTeam1Missing() {
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(teamRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> matchService.create(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Team not found: 10");
    }

    @Test
    void createShouldThrowWhenTeam2Missing() {
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team1));
        when(teamRepository.findById(11L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> matchService.create(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Team not found: 11");
    }

    @Test
    void updateShouldModifyMatchAndInvalidateCache() {
        Match match = new Match();
        match.setId(7L);

        when(matchRepository.findById(7L)).thenReturn(Optional.of(match));
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team1));
        when(teamRepository.findById(11L)).thenReturn(Optional.of(team2));
        when(matchRepository.save(match)).thenReturn(match);

        MatchResponse result = matchService.update(7L, request);

        assertThat(match.getTournament()).isSameAs(tournament);
        assertThat(match.getTeam1()).isSameAs(team1);
        assertThat(match.getTeam2()).isSameAs(team2);
        assertThat(result.getTeam1Id()).isEqualTo(10L);
        verify(matchSearchIndex).invalidateAll();
    }

    @Test
    void updateShouldClearNullableRelations() {
        Match match = new Match();
        match.setId(8L);
        match.setTournament(tournament);
        match.setTeam1(team1);
        match.setTeam2(team2);

        MatchRequest nullableRequest = new MatchRequest(
                null,
                null,
                null,
                1,
                0,
                LocalDateTime.of(2026, 3, 17, 10, 0)
        );

        when(matchRepository.findById(8L)).thenReturn(Optional.of(match));
        when(matchRepository.save(match)).thenReturn(match);

        MatchResponse result = matchService.update(8L, nullableRequest);

        assertThat(match.getTournament()).isNull();
        assertThat(match.getTeam1()).isNull();
        assertThat(match.getTeam2()).isNull();
        assertThat(result.getTournamentId()).isNull();
    }

    @Test
    void updateShouldThrowWhenMatchMissing() {
        when(matchRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> matchService.update(99L, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Match not found: 99");
    }

    @Test
    void updateShouldThrowWhenTournamentMissing() {
        Match match = new Match();
        when(matchRepository.findById(7L)).thenReturn(Optional.of(match));
        when(tournamentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> matchService.update(7L, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Tournament not found: 1");
    }

    @Test
    void updateShouldThrowWhenTeam1Missing() {
        Match match = new Match();
        when(matchRepository.findById(7L)).thenReturn(Optional.of(match));
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(teamRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> matchService.update(7L, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Team not found: 10");
    }

    @Test
    void updateShouldThrowWhenTeam2Missing() {
        Match match = new Match();
        when(matchRepository.findById(7L)).thenReturn(Optional.of(match));
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team1));
        when(teamRepository.findById(11L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> matchService.update(7L, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Team not found: 11");
    }

    @Test
    void getByIdShouldReturnMappedMatch() {
        Match match = new Match();
        match.setId(12L);
        match.setTournament(tournament);
        match.setTeam1(team1);
        match.setTeam2(team2);

        when(matchRepository.findById(12L)).thenReturn(Optional.of(match));

        MatchResponse result = matchService.getById(12L);

        assertThat(result.getTeam2Id()).isEqualTo(11L);
    }

    @Test
    void getByIdShouldThrowWhenMissing() {
        when(matchRepository.findById(13L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> matchService.getById(13L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Match not found: 13");
    }

    @Test
    void getAllShouldReturnMappedMatches() {
        Match match = new Match();
        match.setId(14L);

        when(matchRepository.findAll()).thenReturn(List.of(match));

        assertThat(matchService.getAll()).hasSize(1);
    }

    @Test
    void getByTournamentIdShouldReturnEmptyWhenNull() {
        assertThat(matchService.getByTournamentId(null)).isEmpty();
        verify(tournamentRepository, never()).existsById(any());
    }

    @Test
    void getByTournamentIdShouldThrowWhenMissingTournament() {
        when(tournamentRepository.existsById(15L)).thenReturn(false);

        assertThatThrownBy(() -> matchService.getByTournamentId(15L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Tournament not found: 15");
    }

    @Test
    void getByTournamentIdShouldReturnMappedMatches() {
        Match match = new Match();
        match.setId(16L);

        when(tournamentRepository.existsById(1L)).thenReturn(true);
        when(matchRepository.findByTournamentId(1L)).thenReturn(List.of(match));

        assertThat(matchService.getByTournamentId(1L)).hasSize(1);
    }

    @Test
    void getAllPagedShouldMapPage() {
        Match match = new Match();
        match.setId(17L);
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Match> page = new PageImpl<>(List.of(match), pageable, 1);

        when(matchRepository.findAll(pageable)).thenReturn(page);

        assertThat(matchService.getAllPaged(pageable).getContent()).hasSize(1);
    }

    @Test
    void searchByFiltersJpqlShouldReturnCachedPageWhenPresent() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<MatchResponse> cachedPage = new PageImpl<>(List.of(new MatchResponse()), pageable, 1);

        when(matchSearchIndex.get(any())).thenReturn(Optional.of(cachedPage));

        Page<MatchResponse> result = matchService.searchByFiltersJpql("cs", "major", null, null, pageable);

        assertThat(result).isSameAs(cachedPage);
        verify(matchRepository, never()).findByFiltersJpql(anyString(), anyString(), any(), any(), any());
    }

    @Test
    void searchByFiltersJpqlShouldQueryRepositoryAndCacheOnMiss() {
        PageRequest pageable = PageRequest.of(0, 10);
        Match match = new Match();
        match.setId(18L);
        Page<Match> page = new PageImpl<>(List.of(match), pageable, 1);

        when(matchSearchIndex.get(any())).thenReturn(Optional.empty());
        when(matchRepository.findByFiltersJpql(anyString(), anyString(), any(), any(), any())).thenReturn(page);

        Page<MatchResponse> result = matchService.searchByFiltersJpql("CS", "Major", null, null, pageable);

        assertThat(result.getContent()).hasSize(1);
        verify(matchSearchIndex).put(any(), any());
    }

    @Test
    void searchByFiltersNativeShouldQueryNativeRepositoryAndUseDefaults() {
        PageRequest pageable = PageRequest.of(0, 5);
        Page<Match> page = new PageImpl<>(List.of(new Match()), pageable, 1);

        when(matchSearchIndex.get(any())).thenReturn(Optional.empty());
        when(matchRepository.findByFiltersNative(anyString(), anyString(), any(), any(), any())).thenReturn(page);

        Page<MatchResponse> result = matchService.searchByFiltersNative(null, " ", null, null, pageable);

        assertThat(result.getContent()).hasSize(1);
        verify(matchRepository).findByFiltersNative(anyString(), anyString(), any(), any(), any());
    }

    @Test
    void getByFiltersShouldUseSelectedQueryType() {
        PageRequest pageable = PageRequest.of(0, 3);
        Page<Match> page = new PageImpl<>(List.of(new Match()), pageable, 1);

        when(matchSearchIndex.get(any())).thenReturn(Optional.empty());
        when(matchRepository.findByFiltersNative(anyString(), anyString(), any(), any(), any())).thenReturn(page);

        Page<MatchResponse> result = matchService.getByFilters(
                "abc",
                "def",
                LocalDateTime.of(2026, 1, 1, 0, 0),
                LocalDateTime.of(2026, 12, 31, 0, 0),
                pageable,
                MatchSearchQueryType.NATIVE
        );

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void deleteShouldRemoveMatchAndInvalidateCache() {
        when(matchRepository.existsById(20L)).thenReturn(true);

        matchService.delete(20L);

        verify(matchRepository).deleteById(20L);
        verify(matchSearchIndex).invalidateAll();
    }

    @Test
    void deleteShouldThrowWhenMissing() {
        when(matchRepository.existsById(21L)).thenReturn(false);

        assertThatThrownBy(() -> matchService.delete(21L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Match not found: 21");
    }
}
