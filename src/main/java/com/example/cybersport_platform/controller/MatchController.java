package com.example.cybersport_platform.controller;

import com.example.cybersport_platform.dto.request.MatchRequest;
import com.example.cybersport_platform.dto.response.MatchResponse;
import com.example.cybersport_platform.service.MatchSearchQueryType;
import com.example.cybersport_platform.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matches")
@Tag(name = "Matches", description = "Operations with matches")
public class MatchController {

    private final MatchService service;

    @PostMapping
    @Operation(summary = "Create match")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Match created"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<MatchResponse> create(@Valid @RequestBody MatchRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update match")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Match updated"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "404", description = "Match not found")
    })
    public ResponseEntity<MatchResponse> update(@PathVariable Long id, @Valid @RequestBody MatchRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get match by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Match received"),
        @ApiResponse(responseCode = "404", description = "Match not found")
    })
    public ResponseEntity<MatchResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get all matches")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Matches received")
    })
    public ResponseEntity<List<MatchResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/tournament/{tournamentId}")
    @Operation(summary = "Get matches by tournament id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Matches received"),
        @ApiResponse(responseCode = "404", description = "Tournament not found")
    })
    public ResponseEntity<List<MatchResponse>> getByTournamentId(@PathVariable Long tournamentId) {
        return ResponseEntity.ok(service.getByTournamentId(tournamentId));
    }

    @GetMapping("/filter")
    @Operation(summary = "Filter matches by nested fields with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Matches received"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<Page<MatchResponse>> filter(
            @RequestParam(required = false) String gameName,
            @RequestParam(required = false) String tournamentName,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime playedFrom,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime playedTo,
            @RequestParam(defaultValue = "JPQL") MatchSearchQueryType queryType,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.getByFilters(
                gameName,
                tournamentName,
                playedFrom,
                playedTo,
                pageable,
                queryType
        ));
    }

    @GetMapping("/search/jpql")
    @Operation(summary = "Filter matches by nested fields using JPQL")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Matches received"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<Page<MatchResponse>> searchJpql(
            @RequestParam(required = false) String gameName,
            @RequestParam(required = false) String tournamentName,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime playedFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime playedTo,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.searchByFiltersJpql(
                gameName,
                tournamentName,
                playedFrom,
                playedTo,
                pageable
        ));
    }

    @GetMapping("/search/native")
    @Operation(summary = "Filter matches by nested fields using native query")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Matches received"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<Page<MatchResponse>> searchNative(
            @RequestParam(required = false) String gameName,
            @RequestParam(required = false) String tournamentName,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime playedFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime playedTo,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.searchByFiltersNative(
                gameName,
                tournamentName,
                playedFrom,
                playedTo,
                pageable
        ));
    }

    @GetMapping("/page")
    @Operation(summary = "Get matches with pagination only")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Matches received")
    })
    public ResponseEntity<Page<MatchResponse>> getPage(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(service.getAllPaged(pageable));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete match")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Match deleted"),
        @ApiResponse(responseCode = "404", description = "Match not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
