import type { Match, Team, Tournament } from "@/types/entities";

export function enrichMatches(matches: Match[], teams: Team[], tournaments: Tournament[]) {
  return matches.map((match) => ({
    ...match,
    team1Name: teams.find((team) => team.id === match.team1Id)?.name ?? "Unknown team",
    team2Name: teams.find((team) => team.id === match.team2Id)?.name ?? "Unknown team",
    tournamentName:
      tournaments.find((tournament) => tournament.id === match.tournamentId)?.name ??
      "Unknown tournament",
  }));
}

