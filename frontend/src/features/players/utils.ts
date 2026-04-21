import type { Player, Team } from "@/types/entities";

export function enrichPlayers(players: Player[], teams: Team[]) {
  return players.map((player) => {
    const team = teams.find((entry) => entry.id === player.teamId);
    return {
      ...player,
      teamName: player.teamName ?? team?.name ?? "Unassigned",
    };
  });
}

