package com.example.cybersport_platform.cache;

import com.example.cybersport_platform.dto.response.MatchResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class MatchSearchIndex {

    private final Map<MatchSearchCacheKey, Page<MatchResponse>> index = new HashMap<>();

    public synchronized Optional<Page<MatchResponse>> get(MatchSearchCacheKey key) {
        return Optional.ofNullable(index.get(key));
    }

    public synchronized void put(MatchSearchCacheKey key, Page<MatchResponse> value) {
        index.put(key, value);
    }

    public synchronized void invalidateAll() {
        index.clear();
    }
}
