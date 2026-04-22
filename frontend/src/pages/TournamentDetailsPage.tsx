import { Link, useParams } from "react-router-dom";
import { ArrowLeft } from "lucide-react";
import { TeamAvatar } from "@/components/teams/TeamAvatar";
import { Card } from "@/components/ui/Card";
import { Badge } from "@/components/ui/Badge";
import { ErrorState } from "@/components/data/ErrorState";
import { SkeletonGrid } from "@/components/data/SkeletonGrid";
import { useGames } from "@/features/games/hooks";
import { useMatches } from "@/features/matches/hooks";
import { useTournament } from "@/features/tournaments/hooks";
import { formatDate, getTournamentStatus } from "@/lib/format";

export function TournamentDetailsPage() {
  const params = useParams();
  const tournamentId = Number(params.tournamentId);
  const tournamentQuery = useTournament(tournamentId);
  const matchesQuery = useMatches();
  const gamesQuery = useGames();

  if (tournamentQuery.isLoading || matchesQuery.isLoading || gamesQuery.isLoading) {
    return <SkeletonGrid count={4} />;
  }

  if (tournamentQuery.isError || matchesQuery.isError || gamesQuery.isError || !tournamentQuery.data || !matchesQuery.data || !gamesQuery.data) {
    return <ErrorState description="Tournament details could not be loaded." />;
  }

  const tournament = tournamentQuery.data;
  const matches = matchesQuery.data.filter((match) => match.tournamentId === tournament.id);
  const game = gamesQuery.data.find((entry) => entry.id === tournament.gameId);

  return (
    <>
      <Link
        to="/tournaments"
        className="inline-flex w-fit items-center gap-2 rounded-2xl px-3 py-2 text-sm text-white/70 transition hover:bg-white/5 hover:text-white"
      >
        <ArrowLeft className="h-4 w-4" />
        Back to tournaments
      </Link>
      <Card tone="accent">
        <div className="flex flex-col gap-6 lg:flex-row lg:items-end lg:justify-between">
          <div>
            <p className="text-[11px] uppercase tracking-[0.32em] text-accent/80">Tournament details</p>
            <h2 className="mt-4 font-display text-6xl font-bold tracking-wide text-white">{tournament.name}</h2>
            <p className="mt-3 text-sm text-white/60">
              {formatDate(tournament.startDate)} - {formatDate(tournament.endDate)}
            </p>
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
            {getTournamentStatus(tournament)}
          </Badge>
        </div>
      </Card>
      <div className="grid gap-6 lg:grid-cols-[0.9fr_1.1fr]">
        <Card>
          <h3 className="font-display text-3xl font-bold text-white">Event profile</h3>
          <div className="mt-6 grid gap-4">
            <div className="rounded-2xl border border-white/8 bg-white/[0.03] p-4">
              <p className="text-xs uppercase tracking-[0.2em] text-white/30">Game</p>
              <p className="mt-2 text-white">{game?.name ?? tournament.gameName}</p>
            </div>
            <div className="rounded-2xl border border-white/8 bg-white/[0.03] p-4">
              <p className="text-xs uppercase tracking-[0.2em] text-white/30">Prize pool</p>
              <p className="mt-2 font-display text-3xl text-white">{tournament.prizePool}</p>
            </div>
            <div className="rounded-2xl border border-white/8 bg-white/[0.03] p-4">
              <p className="text-xs uppercase tracking-[0.2em] text-white/30">Matches</p>
              <p className="mt-2 text-white">{matches.length}</p>
            </div>
            <div className="rounded-2xl border border-white/8 bg-white/[0.03] p-4">
              <p className="text-xs uppercase tracking-[0.2em] text-white/30">Teams</p>
              <p className="mt-2 text-white">{tournament.teams?.length ?? 0}</p>
            </div>
          </div>
        </Card>
        <Card>
          <div className="flex items-center justify-between">
            <h3 className="font-display text-3xl font-bold text-white">Related matches</h3>
            <Badge tone="accent">{matches.length}</Badge>
          </div>
          <div className="mt-6 space-y-3">
            {matches.map((match) => (
              <Link
                key={match.id}
                to={`/matches/${match.id}`}
                className="block rounded-2xl border border-white/8 bg-white/[0.03] p-4 transition hover:border-white/15"
              >
                <p className="font-semibold text-white">Match #{match.id}</p>
                <p className="mt-1 text-sm text-white/70">{formatDate(match.playedAt)}</p>
              </Link>
            ))}
            {!matches.length ? <p className="text-sm text-white/65">No matches are linked to this tournament yet.</p> : null}
          </div>
        </Card>
      </div>
      <Card>
        <div className="flex items-center justify-between">
          <h3 className="font-display text-3xl font-bold text-white">Participating teams</h3>
          <Badge tone="accent">{tournament.teams?.length ?? 0}</Badge>
        </div>
        <div className="mt-6 space-y-3">
          {tournament.teams?.map((team) => (
            <Link
              key={team.id}
              to={`/teams/${team.id}`}
              className="flex items-center justify-between rounded-2xl border border-white/8 bg-white/[0.03] p-4 transition hover:border-white/15"
            >
              <div className="flex items-center gap-3">
                <TeamAvatar name={team.name} gameName={team.gameName} size="md" />
                <div>
                  <p className="font-semibold text-white">{team.name}</p>
                  <p className="mt-1 text-sm text-white/70">{team.gameName}</p>
                </div>
              </div>
              <span className="text-sm text-white/75">Team #{team.id}</span>
            </Link>
          ))}
          {!tournament.teams?.length ? <p className="text-sm text-white/65">No teams linked to this tournament yet.</p> : null}
        </div>
      </Card>
    </>
  );
}
