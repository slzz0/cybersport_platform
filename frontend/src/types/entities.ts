export interface Game {
  id: number;
  name: string;
  description: string;
}

export interface Player {
  id: number;
  nickname: string;
  elo: number;
  teamId: number;
  teamName?: string;
}

export interface PlayerPayload {
  nickname: string;
  elo: number;
  teamId: number;
}

export interface Team {
  id: number;
  name: string;
  gameId: number;
  gameName?: string;
  tournaments?: TournamentSummary[];
}

export interface TeamPayload {
  name: string;
  gameId: number;
}

export interface Match {
  id: number;
  tournamentId: number;
  team1Id: number;
  team2Id: number;
  scoreTeam1: number;
  scoreTeam2: number;
  playedAt: string;
}

export interface MatchPayload {
  tournamentId: number;
  team1Id: number;
  team2Id: number;
  scoreTeam1: number;
  scoreTeam2: number;
  playedAt: string;
}

export interface Tournament {
  id: number;
  name: string;
  startDate: string;
  endDate: string;
  prizePool: string;
  gameId: number;
  gameName?: string;
  teams?: TeamSummary[];
}

export interface TournamentPayload {
  name: string;
  startDate: string;
  endDate: string;
  prizePool: string;
  gameId: number;
}

export interface TeamSummary {
  id: number;
  name: string;
  gameId: number;
  gameName?: string;
}

export interface TournamentSummary {
  id: number;
  name: string;
  startDate: string;
  endDate: string;
  prizePool: string;
  gameId: number;
  gameName?: string;
}
