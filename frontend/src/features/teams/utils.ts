import type { Game, Player, Team } from "@/types/entities";

export function enrichTeams(teams: Team[], games: Game[]) {
  return teams.map((team) => ({
    ...team,
    gameName: team.gameName ?? games.find((game) => game.id === team.gameId)?.name ?? "Unknown game",
  }));
}

export function getTeamPlayers(teamId: number, players: Player[]) {
  return players.filter((player) => player.teamId === teamId);
}

