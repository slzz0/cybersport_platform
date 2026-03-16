package com.example.cybersport_platform.cache;

import com.example.cybersport_platform.service.MatchSearchQueryType;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Objects;

public class MatchSearchCacheKey {

    private final String gameName;
    private final String tournamentName;
    private final LocalDateTime playedFrom;
    private final LocalDateTime playedTo;
    private final int page;
    private final int size;
    private final String sort;
    private final MatchSearchQueryType queryType;

    public MatchSearchCacheKey(
            String gameName,
            String tournamentName,
            LocalDateTime playedFrom,
            LocalDateTime playedTo,
            Pageable pageable,
            MatchSearchQueryType queryType
    ) {
        this.gameName = gameName;
        this.tournamentName = tournamentName;
        this.playedFrom = playedFrom;
        this.playedTo = playedTo;
        this.page = pageable.getPageNumber();
        this.size = pageable.getPageSize();
        this.sort = pageable.getSort().toString();
        this.queryType = queryType;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        MatchSearchCacheKey that = (MatchSearchCacheKey) object;
        return page == that.page
                && size == that.size
                && Objects.equals(gameName, that.gameName)
                && Objects.equals(tournamentName, that.tournamentName)
                && Objects.equals(playedFrom, that.playedFrom)
                && Objects.equals(playedTo, that.playedTo)
                && Objects.equals(sort, that.sort)
                && queryType == that.queryType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameName, tournamentName, playedFrom, playedTo, page, size, sort, queryType);
    }
}
