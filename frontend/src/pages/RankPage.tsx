import { Link } from "react-router-dom";
import { Crown, Flame, Shield, Trophy, UserSquare2 } from "lucide-react";
import { TeamAvatar } from "@/components/teams/TeamAvatar";
import { Badge } from "@/components/ui/Badge";
import { Button } from "@/components/ui/Button";
import { Card } from "@/components/ui/Card";
import { SectionHeading } from "@/components/ui/SectionHeading";
import { ErrorState } from "@/components/data/ErrorState";
import { SkeletonGrid } from "@/components/data/SkeletonGrid";
import { useDashboardData } from "@/features/dashboard/hooks";
import { enrichMatches } from "@/features/matches/utils";
import { enrichPlayers } from "@/features/players/utils";
import { enrichTeams, getTeamPlayers } from "@/features/teams/utils";
import { enrichTournaments } from "@/features/tournaments/utils";
import { formatDateTime, getMatchStatus, getTournamentStatus } from "@/lib/format";

function parsePrizePool(value: string) {
  const normalized = value.replace(/[^0-9.]/g, "");
  const amount = Number(normalized);
  return Number.isNaN(amount) ? 0 : amount;
}

function buildTeamRankings(
  teams: ReturnType<typeof enrichTeams>,
  players: ReturnType<typeof enrichPlayers>,
) {
  return teams
    .map((team) => {
      const roster = getTeamPlayers(team.id, players);
      const averageElo = roster.length
        ? Math.round(roster.reduce((sum, player) => sum + player.elo, 0) / roster.length)
        : 0;

      return {
        ...team,
        rosterCount: roster.length,
        averageElo,
      };
    })
    .sort((left, right) => {
      if (right.averageElo !== left.averageElo) {
        return right.averageElo - left.averageElo;
      }

      return right.rosterCount - left.rosterCount;
    });
}

export function RankPage() {
  const { data, isLoading, isError, refetch } = useDashboardData();

  if (isLoading) {
    return <SkeletonGrid count={6} />;
  }

  if (isError || !data) {
    return (
      <ErrorState
        description="Rank surfaces are unavailable right now."
        action={<Button onClick={() => refetch()}>Retry</Button>}
      />
    );
  }

  const teams = enrichTeams(data.teams, data.games);
  const players = enrichPlayers(data.players, teams);
  const tournaments = enrichTournaments(data.tournaments, data.games, data.matches);
  const matches = enrichMatches(data.matches, teams, data.tournaments);

  const rankedPlayers = players.slice().sort((left, right) => right.elo - left.elo).slice(0, 8);
  const rankedTeams = buildTeamRankings(teams, players).slice(0, 8);
  const rankedTournaments = tournaments
    .slice()
    .sort((left, right) => {
      if (right.matchCount !== left.matchCount) {
        return right.matchCount - left.matchCount;
      }

      return parsePrizePool(right.prizePool) - parsePrizePool(left.prizePool);
    })
    .slice(0, 6);
  const rankedMatches = matches
    .slice()
    .sort((left, right) => {
      const statusWeight = (value: string) => {
        if (value === "Live") return 3;
        if (value === "Upcoming") return 2;
        return 1;
      };

      return statusWeight(getMatchStatus(right)) - statusWeight(getMatchStatus(left));
    })
    .slice(0, 6);

  const topPlayer = rankedPlayers[0];
  const topTeam = rankedTeams[0];

  return (
    <>
      <SectionHeading
        eyebrow="Rank mode"
        title="Platform leaderboards"
        description="A dedicated ranking lane for the strongest players, top teams, biggest tournament tracks and hottest matchups."
      />

      <section className="grid gap-6 xl:grid-cols-[1.15fr_0.85fr]">
        <Card tone="accent" className="overflow-hidden">
          <div className="flex items-start justify-between gap-4">
            <div>
              <p className="text-[11px] uppercase tracking-[0.32em] text-accent/80">Top player</p>
              <h3 className="mt-4 font-display text-5xl font-bold tracking-wide text-white">
                {topPlayer?.nickname ?? "No player data"}
              </h3>
              <p className="mt-3 text-sm text-white/85">
                Highest visible ELO on the platform right now with direct team context.
              </p>
            </div>
            <div className="rounded-2xl border border-white/10 bg-black/20 p-3">
              <Crown className="h-5 w-5 text-accent" />
            </div>
          </div>

          {topPlayer ? (
            <div className="mt-8 grid gap-4 sm:grid-cols-3">
              <div className="rounded-[24px] border border-white/10 bg-black/20 p-5">
                <p className="text-xs uppercase tracking-[0.2em] text-white/30">ELO</p>
                <p className="mt-3 font-display text-5xl font-bold text-white">{topPlayer.elo}</p>
              </div>
              <div className="rounded-[24px] border border-white/10 bg-black/20 p-5">
                <p className="text-xs uppercase tracking-[0.2em] text-white/30">Team</p>
                <p className="mt-3 text-lg font-semibold text-white">{topPlayer.teamName ?? "Unassigned"}</p>
              </div>
              <div className="rounded-[24px] border border-white/10 bg-black/20 p-5">
                <p className="text-xs uppercase tracking-[0.2em] text-white/30">Profile</p>
                <Link to={`/players/${topPlayer.id}`} className="mt-3 inline-flex text-sm font-medium text-accent">
                  Open player details
                </Link>
              </div>
            </div>
          ) : null}
        </Card>

        <Card>
          <p className="text-[11px] uppercase tracking-[0.3em] text-white/60">Top team</p>
          <div className="mt-3 flex items-center gap-4">
            {topTeam ? <TeamAvatar name={topTeam.name} gameName={topTeam.gameName} size="lg" /> : null}
            <h3 className="font-display text-3xl font-bold tracking-wide text-white">{topTeam?.name ?? "No team data"}</h3>
          </div>
          {topTeam ? (
            <>
              <div className="mt-6 grid grid-cols-2 gap-3">
                <div className="rounded-2xl border border-white/8 bg-white/[0.03] p-4">
                  <p className="text-xs uppercase tracking-[0.2em] text-white/30">Avg ELO</p>
                  <p className="mt-2 font-display text-3xl text-white">{topTeam.averageElo}</p>
                </div>
                <div className="rounded-2xl border border-white/8 bg-white/[0.03] p-4">
                  <p className="text-xs uppercase tracking-[0.2em] text-white/30">Roster</p>
                  <p className="mt-2 font-display text-3xl text-white">{topTeam.rosterCount}</p>
                </div>
              </div>
              <div className="mt-4 rounded-2xl border border-white/8 bg-white/[0.03] p-4">
                <p className="text-xs uppercase tracking-[0.2em] text-white/30">Discipline</p>
                <p className="mt-2 text-white">{topTeam.gameName}</p>
              </div>
              <Link to={`/teams/${topTeam.id}`} className="mt-6 inline-flex text-sm font-medium text-accent">
                Open team details
              </Link>
            </>
          ) : null}
        </Card>
      </section>

      <section className="grid gap-6 xl:grid-cols-2">
        <Card>
          <div className="mb-5 flex items-center justify-between">
            <div>
              <p className="text-[11px] uppercase tracking-[0.28em] text-white/60">Players ladder</p>
              <h3 className="mt-2 font-display text-3xl font-bold text-white">Top players</h3>
            </div>
            <UserSquare2 className="h-5 w-5 text-accent" />
          </div>
          <div className="space-y-3">
            {rankedPlayers.map((player, index) => (
              <Link
                key={player.id}
                to={`/players/${player.id}`}
                className="flex items-center justify-between rounded-2xl border border-white/8 bg-white/[0.03] p-4 transition hover:border-accent/18 hover:bg-white/[0.05]"
              >
                <div className="flex items-center gap-4">
                  <span className="w-8 text-sm text-white/85">#{index + 1}</span>
                  <div>
                    <p className="font-semibold text-white">{player.nickname}</p>
                    <p className="mt-1 text-sm text-white/65">{player.teamName ?? "Unassigned"}</p>
                  </div>
                </div>
                <Badge tone={index < 3 ? "accent" : "neutral"}>{player.elo} ELO</Badge>
              </Link>
            ))}
          </div>
        </Card>

        <Card>
          <div className="mb-5 flex items-center justify-between">
            <div>
              <p className="text-[11px] uppercase tracking-[0.28em] text-white/60">Squad power</p>
              <h3 className="mt-2 font-display text-3xl font-bold text-white">Top teams</h3>
            </div>
            <Shield className="h-5 w-5 text-accent" />
          </div>
          <div className="space-y-3">
            {rankedTeams.map((team, index) => (
              <Link
                key={team.id}
                to={`/teams/${team.id}`}
                className="flex items-center justify-between rounded-2xl border border-white/8 bg-white/[0.03] p-4 transition hover:border-accent/18 hover:bg-white/[0.05]"
              >
                <div className="flex items-center gap-4">
                  <span className="w-8 text-sm text-white/85">#{index + 1}</span>
                  <TeamAvatar name={team.name} gameName={team.gameName} size="md" />
                  <div>
                    <p className="font-semibold text-white">{team.name}</p>
                    <p className="mt-1 text-sm text-white/65">
                      {team.gameName} • {team.rosterCount} players
                    </p>
                  </div>
                </div>
                <Badge tone={index < 3 ? "accent" : "neutral"}>{team.averageElo} avg</Badge>
              </Link>
            ))}
          </div>
        </Card>
      </section>

      <section className="grid gap-6 xl:grid-cols-2">
        <Card>
          <div className="mb-5 flex items-center justify-between">
            <div>
              <p className="text-[11px] uppercase tracking-[0.28em] text-white/60">Event heat</p>
              <h3 className="mt-2 font-display text-3xl font-bold text-white">Top tournaments</h3>
            </div>
            <Trophy className="h-5 w-5 text-accent" />
          </div>
          <div className="space-y-3">
            {rankedTournaments.map((tournament, index) => (
              <Link
                key={tournament.id}
                to={`/tournaments/${tournament.id}`}
                className="flex items-center justify-between rounded-2xl border border-white/8 bg-white/[0.03] p-4 transition hover:border-accent/18 hover:bg-white/[0.05]"
              >
                <div className="flex items-center gap-4">
                  <span className="w-8 text-sm text-white/85">#{index + 1}</span>
                  <div>
                    <p className="font-semibold text-white">{tournament.name}</p>
                    <p className="mt-1 text-sm text-white/65">
                      {tournament.gameName} • {tournament.matchCount} matches
                    </p>
                  </div>
                </div>
                <Badge
                  tone={
                    getTournamentStatus(tournament) === "Active"
                      ? "success"
                      : getTournamentStatus(tournament) === "Upcoming"
                        ? "accent"
                        : "neutral"
                  }
                >
                  {tournament.prizePool}
                </Badge>
              </Link>
            ))}
          </div>
        </Card>

        <Card>
          <div className="mb-5 flex items-center justify-between">
            <div>
              <p className="text-[11px] uppercase tracking-[0.28em] text-white/60">Arena pulse</p>
              <h3 className="mt-2 font-display text-3xl font-bold text-white">Hot matches</h3>
            </div>
            <Flame className="h-5 w-5 text-accent" />
          </div>
          <div className="space-y-3">
            {rankedMatches.map((match, index) => (
              <Link
                key={match.id}
                to={`/matches/${match.id}`}
                className="flex items-center justify-between rounded-2xl border border-white/8 bg-white/[0.03] p-4 transition hover:border-accent/18 hover:bg-white/[0.05]"
              >
                <div className="flex items-center gap-4">
                  <span className="w-8 text-sm text-white/85">#{index + 1}</span>
                  <div>
                    <div className="flex items-center gap-2">
                      <TeamAvatar name={match.team1Name} size="sm" />
                      <p className="font-semibold text-white">{match.team1Name}</p>
                      <span className="text-xs uppercase tracking-[0.2em] text-white/30">vs</span>
                      <p className="font-semibold text-white">{match.team2Name}</p>
                      <TeamAvatar name={match.team2Name} size="sm" />
                    </div>
                    <p className="mt-1 text-sm text-white/65">{formatDateTime(match.playedAt)}</p>
                  </div>
                </div>
                <div className="text-right">
                  <Badge tone={getMatchStatus(match) === "Live" ? "danger" : "accent"}>
                    {getMatchStatus(match)}
                  </Badge>
                  <p className="mt-2 text-xs text-white/85">{match.tournamentName}</p>
                </div>
              </Link>
            ))}
          </div>
        </Card>
      </section>
    </>
  );
}
