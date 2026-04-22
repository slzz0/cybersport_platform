import { Link, useParams } from "react-router-dom";
import { ArrowLeft } from "lucide-react";
import { TeamAvatar } from "@/components/teams/TeamAvatar";
import { Card } from "@/components/ui/Card";
import { Badge } from "@/components/ui/Badge";
import { ErrorState } from "@/components/data/ErrorState";
import { SkeletonGrid } from "@/components/data/SkeletonGrid";
import { usePlayer } from "@/features/players/hooks";
import { useTeam } from "@/features/teams/hooks";

export function PlayerDetailsPage() {
  const params = useParams();
  const playerId = Number(params.playerId);
  const playerQuery = usePlayer(playerId);
  const teamQuery = useTeam(playerQuery.data?.teamId ?? 0);

  if (playerQuery.isLoading) {
    return <SkeletonGrid count={2} />;
  }

  if (playerQuery.isError || !playerQuery.data) {
    return <ErrorState description="Player details could not be loaded." />;
  }

  const player = playerQuery.data;

  return (
    <>
      <Link
        to="/players"
        className="inline-flex w-fit items-center gap-2 rounded-2xl px-3 py-2 text-sm text-white/70 transition hover:bg-white/5 hover:text-white"
      >
        <ArrowLeft className="h-4 w-4" />
        Back to players
      </Link>
      <Card tone="accent">
        <div className="grid gap-6 lg:grid-cols-[1fr_0.8fr]">
          <div>
            <p className="text-[11px] uppercase tracking-[0.32em] text-accent/80">Player profile</p>
            <h2 className="mt-4 font-display text-6xl font-bold tracking-wide text-white">{player.nickname}</h2>
            <p className="mt-3 max-w-xl text-sm text-white/60">
              Detailed roster view with current team assignment and rating snapshot.
            </p>
          </div>
          <div className="grid gap-4 sm:grid-cols-2">
            <div className="rounded-[24px] border border-white/10 bg-black/20 p-5">
              <p className="text-xs uppercase tracking-[0.2em] text-white/75">ELO</p>
              <p className="mt-3 font-display text-5xl font-bold text-white">{player.elo}</p>
            </div>
            <div className="rounded-[24px] border border-white/10 bg-black/20 p-5">
              <p className="text-xs uppercase tracking-[0.2em] text-white/75">Team</p>
              <p className="mt-3 text-lg font-semibold text-white">{player.teamName ?? `#${player.teamId}`}</p>
            </div>
          </div>
        </div>
      </Card>
      <div className="grid gap-6 lg:grid-cols-2">
        <Card>
          <h3 className="font-display text-3xl font-bold text-white">Identity</h3>
          <div className="mt-6 grid gap-4">
            <div className="rounded-2xl border border-white/8 bg-white/[0.03] p-4">
              <p className="text-xs uppercase tracking-[0.2em] text-white/30">Nickname</p>
              <p className="mt-2 text-white">{player.nickname}</p>
            </div>
            <div className="rounded-2xl border border-white/8 bg-white/[0.03] p-4">
              <p className="text-xs uppercase tracking-[0.2em] text-white/30">Team ID</p>
              <p className="mt-2 text-white">{player.teamId}</p>
            </div>
          </div>
        </Card>
        <Card>
          <h3 className="font-display text-3xl font-bold text-white">Team context</h3>
          <div className="mt-6">
            {teamQuery.data ? (
              <Link
                to={`/teams/${teamQuery.data.id}`}
                className="block rounded-2xl border border-white/8 bg-white/[0.03] p-4 transition hover:border-accent/20"
              >
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-3">
                    <TeamAvatar name={teamQuery.data.name} gameName={teamQuery.data.gameName} size="md" />
                    <div>
                      <p className="font-semibold text-white">{teamQuery.data.name}</p>
                      <p className="mt-1 text-sm text-white/70">{teamQuery.data.gameName}</p>
                    </div>
                  </div>
                  <Badge tone="accent">Team #{teamQuery.data.id}</Badge>
                </div>
              </Link>
            ) : (
              <p className="text-sm text-white/65">Team details are unavailable right now.</p>
            )}
          </div>
        </Card>
      </div>
    </>
  );
}
