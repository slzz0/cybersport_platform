import { enrichMatches } from "@/features/matches/utils";
import { enrichPlayers } from "@/features/players/utils";
import { enrichTeams } from "@/features/teams/utils";
import { enrichTournaments } from "@/features/tournaments/utils";
import { getMatchStatus, getTournamentStatus } from "@/lib/format";
import type { Game, Match, Player, Team, Tournament } from "@/types/entities";

export function buildDashboardViewModel(data: {
  games: Game[];
  players: Player[];
  teams: Team[];
  matches: Match[];
  tournaments: Tournament[];
}) {
  const teams = enrichTeams(data.teams, data.games);
  const players = enrichPlayers(data.players, teams);
  const matches = enrichMatches(data.matches, teams, data.tournaments);
  const tournaments = enrichTournaments(data.tournaments, data.games, data.matches);

  const recentItems = [
    ...players.slice(-2).map((player) => ({
      id: `player-${player.id}`,
      title: player.nickname,
      subtitle: `Player added to ${player.teamName ?? "team"}`,
      href: `/players/${player.id}`,
      timestamp: new Date().toISOString(),
    })),
    ...tournaments.slice(-2).map((tournament) => ({
      id: `tournament-${tournament.id}`,
      title: tournament.name,
      subtitle: `${tournament.gameName} tournament`,
      href: `/tournaments/${tournament.id}`,
      timestamp: `${tournament.startDate}T12:00:00`,
    })),
  ];

  return {
    stats: [
      {
        label: "Players",
        value: `${players.length}`,
        trend: `${players.filter((player) => player.elo > 2750).length} elite-rated rosters`,
      },
      {
        label: "Teams",
        value: `${teams.length}`,
        trend: `${teams.filter((team) => team.gameName === "Counter-Strike 2").length} CS2 squads active`,
      },
      {
        label: "Matches",
        value: `${matches.length}`,
        trend: `${matches.filter((match) => getMatchStatus(match) !== "Finished").length} fixtures still on deck`,
      },
      {
        label: "Tournaments",
        value: `${tournaments.length}`,
        trend: `${tournaments.filter((tournament) => getTournamentStatus(tournament) === "Active").length} active events`,
      },
    ],
    recentItems,
    spotlightMatches: matches.slice(0, 3),
    spotlightTournaments: tournaments.slice(0, 3),
  };
}

