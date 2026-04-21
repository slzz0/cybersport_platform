import { getMatchStatus, getTournamentStatus } from "@/lib/format";
import type { Match, Player, Team, Tournament } from "@/types/entities";
import type { SearchResultGroup } from "@/types/ui";

export function buildSearchGroups({
  query,
  players,
  teams,
  matches,
  tournaments,
}: {
  query: string;
  players: Player[];
  teams: Team[];
  matches: Array<Match & { team1Name: string; team2Name: string; tournamentName: string }>;
  tournaments: Tournament[];
}) {
  const search = query.trim().toLowerCase();
  if (!search) {
    return [] as SearchResultGroup<unknown>[];
  }

  return [
    {
      title: "Players",
      type: "players" as const,
      items: players.filter(
        (player) =>
          player.nickname.toLowerCase().includes(search) ||
          player.teamName?.toLowerCase().includes(search),
      ),
    },
    {
      title: "Teams",
      type: "teams" as const,
      items: teams.filter(
        (team) =>
          team.name.toLowerCase().includes(search) ||
          team.gameName?.toLowerCase().includes(search),
      ),
    },
    {
      title: "Matches",
      type: "matches" as const,
      items: matches.filter(
        (match) =>
          match.team1Name.toLowerCase().includes(search) ||
          match.team2Name.toLowerCase().includes(search) ||
          match.tournamentName.toLowerCase().includes(search) ||
          getMatchStatus(match).toLowerCase().includes(search),
      ),
    },
    {
      title: "Tournaments",
      type: "tournaments" as const,
      items: tournaments.filter(
        (tournament) =>
          tournament.name.toLowerCase().includes(search) ||
          tournament.gameName?.toLowerCase().includes(search) ||
          getTournamentStatus(tournament).toLowerCase().includes(search),
      ),
    },
  ].filter((group) => group.items.length > 0);
}

