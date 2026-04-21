import type {
  Game,
  Match,
  MatchPayload,
  Player,
  PlayerPayload,
  Team,
  TeamPayload,
  Tournament,
  TournamentSummary,
  TournamentPayload,
  TeamSummary,
} from "@/types/entities";

let games: Game[] = [
  {
    id: 1,
    name: "Counter-Strike 2",
    description: "Tactical FPS with elite-level tournament ecosystems.",
  },
  {
    id: 2,
    name: "Dota 2",
    description: "MOBA discipline built around strategic drafts and long-form series.",
  },
  {
    id: 3,
    name: "VALORANT",
    description: "Fast tactical shooter with agent-based utility play.",
  },
];

let teams: Team[] = [
  { id: 1, name: "Vertex Reign", gameId: 1, gameName: "Counter-Strike 2" },
  { id: 2, name: "Nova Five", gameId: 3, gameName: "VALORANT" },
  { id: 3, name: "Solar Tide", gameId: 2, gameName: "Dota 2" },
  { id: 4, name: "Night Pulse", gameId: 1, gameName: "Counter-Strike 2" },
];

let players: Player[] = [
  { id: 1, nickname: "Riven", elo: 2860, teamId: 1, teamName: "Vertex Reign" },
  { id: 2, nickname: "Axiom", elo: 2795, teamId: 1, teamName: "Vertex Reign" },
  { id: 3, nickname: "Nyx", elo: 2910, teamId: 2, teamName: "Nova Five" },
  { id: 4, nickname: "Sevr", elo: 2675, teamId: 3, teamName: "Solar Tide" },
  { id: 5, nickname: "Drift", elo: 2730, teamId: 4, teamName: "Night Pulse" },
  { id: 6, nickname: "Morrow", elo: 2640, teamId: 2, teamName: "Nova Five" },
];

let tournaments: Tournament[] = [
  {
    id: 1,
    name: "GRACEIT Masters",
    startDate: "2026-05-14",
    endDate: "2026-05-19",
    prizePool: "$120,000",
    gameId: 1,
    gameName: "Counter-Strike 2",
  },
  {
    id: 2,
    name: "Circuit Finals",
    startDate: "2026-04-18",
    endDate: "2026-04-24",
    prizePool: "$85,000",
    gameId: 3,
    gameName: "VALORANT",
  },
  {
    id: 3,
    name: "Aegis Open",
    startDate: "2026-06-03",
    endDate: "2026-06-12",
    prizePool: "$210,000",
    gameId: 2,
    gameName: "Dota 2",
  },
];

let matches: Match[] = [
  {
    id: 1,
    tournamentId: 1,
    team1Id: 1,
    team2Id: 4,
    scoreTeam1: 2,
    scoreTeam2: 1,
    playedAt: "2026-04-21T18:30:00",
  },
  {
    id: 2,
    tournamentId: 2,
    team1Id: 2,
    team2Id: 1,
    scoreTeam1: 1,
    scoreTeam2: 0,
    playedAt: "2026-04-22T20:00:00",
  },
  {
    id: 3,
    tournamentId: 3,
    team1Id: 3,
    team2Id: 2,
    scoreTeam1: 0,
    scoreTeam2: 0,
    playedAt: "2026-06-05T17:00:00",
  },
];

function nextId(collection: Array<{ id: number }>) {
  return Math.max(0, ...collection.map((item) => item.id)) + 1;
}

function resolveTeamName(teamId: number) {
  return teams.find((team) => team.id === teamId)?.name;
}

function resolveGameName(gameId: number) {
  return games.find((game) => game.id === gameId)?.name;
}

function resolveTeamTournamentSummaries(teamId: number): TournamentSummary[] {
  const tournamentIds = new Set(
    matches
      .filter((match) => match.team1Id === teamId || match.team2Id === teamId)
      .map((match) => match.tournamentId),
  );

  return tournaments
    .filter((tournament) => tournamentIds.has(tournament.id))
    .map((tournament) => ({
      id: tournament.id,
      name: tournament.name,
      startDate: tournament.startDate,
      endDate: tournament.endDate,
      prizePool: tournament.prizePool,
      gameId: tournament.gameId,
      gameName: tournament.gameName,
    }));
}

function resolveTournamentTeamSummaries(tournamentId: number): TeamSummary[] {
  const teamIds = new Set(
    matches
      .filter((match) => match.tournamentId === tournamentId)
      .flatMap((match) => [match.team1Id, match.team2Id]),
  );

  return teams
    .filter((team) => teamIds.has(team.id))
    .map((team) => ({
      id: team.id,
      name: team.name,
      gameId: team.gameId,
      gameName: team.gameName,
    }));
}

export const mockDb = {
  getGames: () => [...games],
  getPlayers: () => [...players],
  getTeams: () => teams.map((team) => ({ ...team, tournaments: resolveTeamTournamentSummaries(team.id) })),
  getMatches: () => [...matches],
  getTournaments: () =>
    tournaments.map((tournament) => ({ ...tournament, teams: resolveTournamentTeamSummaries(tournament.id) })),

  getPlayerById: (id: number) => players.find((item) => item.id === id),
  getTeamById: (id: number) => {
    const team = teams.find((item) => item.id === id);
    return team ? { ...team, tournaments: resolveTeamTournamentSummaries(team.id) } : undefined;
  },
  getMatchById: (id: number) => matches.find((item) => item.id === id),
  getTournamentById: (id: number) => {
    const tournament = tournaments.find((item) => item.id === id);
    return tournament ? { ...tournament, teams: resolveTournamentTeamSummaries(tournament.id) } : undefined;
  },

  createPlayer: (payload: PlayerPayload) => {
    const player = {
      id: nextId(players),
      ...payload,
      teamName: resolveTeamName(payload.teamId),
    };
    players = [...players, player];
    return player;
  },

  updatePlayer: (id: number, payload: PlayerPayload) => {
    const player = {
      id,
      ...payload,
      teamName: resolveTeamName(payload.teamId),
    };
    players = players.map((item) => (item.id === id ? player : item));
    return player;
  },

  deletePlayer: (id: number) => {
    players = players.filter((item) => item.id !== id);
  },

  createTeam: (payload: TeamPayload) => {
    const team = {
      id: nextId(teams),
      ...payload,
      gameName: resolveGameName(payload.gameId),
    };
    teams = [...teams, team];
    return team;
  },

  updateTeam: (id: number, payload: TeamPayload) => {
    const team = {
      id,
      ...payload,
      gameName: resolveGameName(payload.gameId),
    };
    teams = teams.map((item) => (item.id === id ? team : item));
    players = players.map((player) =>
      player.teamId === id ? { ...player, teamName: team.name } : player,
    );
    return team;
  },

  deleteTeam: (id: number) => {
    teams = teams.filter((item) => item.id !== id);
  },

  createMatch: (payload: MatchPayload) => {
    const match = {
      id: nextId(matches),
      ...payload,
    };
    matches = [...matches, match];
    return match;
  },

  updateMatch: (id: number, payload: MatchPayload) => {
    const match = { id, ...payload };
    matches = matches.map((item) => (item.id === id ? match : item));
    return match;
  },

  deleteMatch: (id: number) => {
    matches = matches.filter((item) => item.id !== id);
  },

  createTournament: (payload: TournamentPayload) => {
    const tournament = {
      id: nextId(tournaments),
      ...payload,
      gameName: resolveGameName(payload.gameId),
    };
    tournaments = [...tournaments, tournament];
    return tournament;
  },

  updateTournament: (id: number, payload: TournamentPayload) => {
    const tournament = {
      id,
      ...payload,
      gameName: resolveGameName(payload.gameId),
    };
    tournaments = tournaments.map((item) => (item.id === id ? tournament : item));
    return tournament;
  },

  deleteTournament: (id: number) => {
    tournaments = tournaments.filter((item) => item.id !== id);
  },
};
