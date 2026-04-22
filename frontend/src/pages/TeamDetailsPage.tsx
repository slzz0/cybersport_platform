import { Link, useParams } from "react-router-dom";
import { ArrowLeft } from "lucide-react";
import { TeamAvatar } from "@/components/teams/TeamAvatar";
import { Card } from "@/components/ui/Card";
import { Badge } from "@/components/ui/Badge";
import { ErrorState } from "@/components/data/ErrorState";
import { SkeletonGrid } from "@/components/data/SkeletonGrid";
import { usePlayers } from "@/features/players/hooks";
import { useTeam } from "@/features/teams/hooks";
import { getTeamPlayers } from "@/features/teams/utils";

export function TeamDetailsPage() {
  const params = useParams();
  const teamId = Number(params.teamId);
  const teamQuery = useTeam(teamId);
  const playersQuery = usePlayers();

  if (teamQuery.isLoading || playersQuery.isLoading) {
    return <SkeletonGrid count={4} />;
  }

  if (teamQuery.isError || playersQuery.isError || !teamQuery.data || !playersQuery.data) {
    return <ErrorState description="Team details could not be loaded." />;
  }

  const team = teamQuery.data;
  const players = getTeamPlayers(team.id, playersQuery.data);

  return (
    <>
      <Link
        to="/teams"
        className="inline-flex w-fit items-center gap-2 rounded-2xl px-3 py-2 text-sm text-white/70 transition hover:bg-white/5 hover:text-white"
      >
        <ArrowLeft className="h-4 w-4" />
        Back to teams
      </Link>
      <Card tone="accent">
        <div className="grid gap-6 lg:grid-cols-[1fr_0.9fr]">
          <div className="flex items-start gap-4">
            <TeamAvatar name={team.name} gameName={team.gameName} size="xl" />
            <div>
              <p className="text-[11px] uppercase tracking-[0.32em] text-accent/80">Team profile</p>
              <h2 className="mt-4 font-display text-6xl font-bold tracking-wide text-white">{team.name}</h2>
              <p className="mt-3 max-w-xl text-sm text-white/60">
                Team surface with game discipline context and visible player composition.
              </p>
            </div>
          </div>
          <div className="grid gap-4 sm:grid-cols-2">
            <div className="rounded-[24px] border border-white/10 bg-black/20 p-5">
              <p className="text-xs uppercase tracking-[0.2em] text-white/75">Game</p>
              <p className="mt-3 text-lg font-semibold text-white">{team.gameName ?? `Game #${team.gameId}`}</p>
            </div>
            <div className="rounded-[24px] border border-white/10 bg-black/20 p-5">
              <p className="text-xs uppercase tracking-[0.2em] text-white/75">Roster size</p>
              <p className="mt-3 font-display text-5xl font-bold text-white">{players.length}</p>
            </div>
          </div>
        </div>
      </Card>
      <div className="grid gap-6 lg:grid-cols-[0.9fr_1.1fr]">
        <Card>
          <h3 className="font-display text-3xl font-bold text-white">Metadata</h3>
          <div className="mt-6 grid gap-4">
            <div className="rounded-2xl border border-white/8 bg-white/[0.03] p-4">
              <p className="text-xs uppercase tracking-[0.2em] text-white/30">Team ID</p>
              <p className="mt-2 text-white">{team.id}</p>
            </div>
            <div className="rounded-2xl border border-white/8 bg-white/[0.03] p-4">
              <p className="text-xs uppercase tracking-[0.2em] text-white/30">Game ID</p>
              <p className="mt-2 text-white">{team.gameId}</p>
            </div>
            <div className="rounded-2xl border border-white/8 bg-white/[0.03] p-4">
              <p className="text-xs uppercase tracking-[0.2em] text-white/30">Tournaments</p>
              <p className="mt-2 text-white">{team.tournaments?.length ?? 0}</p>
            </div>
          </div>
        </Card>
        <Card>
          <div className="flex items-center justify-between">
            <h3 className="font-display text-3xl font-bold text-white">Players</h3>
            <Badge tone="accent">{players.length} linked</Badge>
          </div>
          <div className="mt-6 space-y-3">
            {players.map((player) => (
              <Link
                key={player.id}
                to={`/players/${player.id}`}
                className="flex items-center justify-between rounded-2xl border border-white/8 bg-white/[0.03] p-4 transition hover:border-white/15"
              >
                <div>
                  <p className="font-semibold text-white">{player.nickname}</p>
                  <p className="mt-1 text-sm text-white/70">ELO {player.elo}</p>
                </div>
                <span className="text-sm text-white/75">Player #{player.id}</span>
              </Link>
            ))}
            {!players.length ? <p className="text-sm text-white/65">No players assigned to this team.</p> : null}
          </div>
        </Card>
      </div>
      <Card>
        <div className="flex items-center justify-between">
          <h3 className="font-display text-3xl font-bold text-white">Tournaments</h3>
          <Badge tone="accent">{team.tournaments?.length ?? 0} linked</Badge>
        </div>
        <div className="mt-6 space-y-3">
          {team.tournaments?.map((tournament) => (
            <Link
              key={tournament.id}
              to={`/tournaments/${tournament.id}`}
              className="flex items-center justify-between rounded-2xl border border-white/8 bg-white/[0.03] p-4 transition hover:border-white/15"
            >
              <div>
                <p className="font-semibold text-white">{tournament.name}</p>
                <p className="mt-1 text-sm text-white/70">
                  {tournament.gameName} • {tournament.prizePool}
                </p>
              </div>
              <span className="text-sm text-white/75">Tournament #{tournament.id}</span>
            </Link>
          ))}
          {!team.tournaments?.length ? <p className="text-sm text-white/65">No tournaments linked to this team.</p> : null}
        </div>
      </Card>
    </>
  );
}
