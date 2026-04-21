import type { Game, Match, Tournament } from "@/types/entities";

export function enrichTournaments(
  tournaments: Tournament[],
  games: Game[],
  matches: Match[],
) {
  return tournaments.map((tournament) => ({
    ...tournament,
    gameName:
      tournament.gameName ??
      games.find((game) => game.id === tournament.gameId)?.name ??
      "Unknown game",
    matchCount: matches.filter((match) => match.tournamentId === tournament.id).length,
  }));
}

