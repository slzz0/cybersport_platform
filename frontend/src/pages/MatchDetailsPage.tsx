import { Link, useParams } from "react-router-dom";
import { ArrowLeft } from "lucide-react";
import { TeamAvatar } from "@/components/teams/TeamAvatar";
import { Card } from "@/components/ui/Card";
import { Badge } from "@/components/ui/Badge";
import { ErrorState } from "@/components/data/ErrorState";
import { SkeletonGrid } from "@/components/data/SkeletonGrid";
import { useMatch } from "@/features/matches/hooks";
import { useTeams } from "@/features/teams/hooks";
import { useTournament } from "@/features/tournaments/hooks";
import { formatDateTime, getMatchStatus } from "@/lib/format";

export function MatchDetailsPage() {
  const params = useParams();
  const matchId = Number(params.matchId);
  const matchQuery = useMatch(matchId);
  const teamsQuery = useTeams();
  const tournamentQuery = useTournament(matchQuery.data?.tournamentId ?? 0);

  if (matchQuery.isLoading || teamsQuery.isLoading) {
    return <SkeletonGrid count={3} />;
  }

  if (matchQuery.isError || teamsQuery.isError || !matchQuery.data || !teamsQuery.data) {
    return <ErrorState description="Match details could not be loaded." />;
  }

  const match = matchQuery.data;
  const team1 = teamsQuery.data.find((team) => team.id === match.team1Id);
  const team2 = teamsQuery.data.find((team) => team.id === match.team2Id);

  return (
    <>
      <Link
        to="/matches"
        className="inline-flex w-fit items-center gap-2 rounded-2xl px-3 py-2 text-sm text-white/70 transition hover:bg-white/5 hover:text-white"
      >
        <ArrowLeft className="h-4 w-4" />
        Back to matches
      </Link>
      <Card tone="accent">
        <div className="flex flex-col gap-6 lg:flex-row lg:items-end lg:justify-between">
          <div>
            <p className="text-[11px] uppercase tracking-[0.32em] text-accent/80">Match details</p>
            <h2 className="mt-4 font-display text-5xl font-bold tracking-wide text-white">
              {team1?.name ?? "Team one"} vs {team2?.name ?? "Team two"}
            </h2>
            <p className="mt-3 text-sm text-white/60">{formatDateTime(match.playedAt)}</p>
          </div>
          <Badge tone={getMatchStatus(match) === "Finished" ? "neutral" : "accent"}>{getMatchStatus(match)}</Badge>
        </div>
      </Card>
      <div className="grid gap-6 lg:grid-cols-[1fr_1fr_0.9fr]">
        {[team1, team2].map((team, index) => (
          <Card key={team?.id ?? index}>
            <p className="text-xs uppercase tracking-[0.2em] text-white/75">Team {index + 1}</p>
            <div className="mt-3 flex items-center gap-3">
              {team ? <TeamAvatar name={team.name} gameName={team.gameName} size="lg" /> : null}
              <p className="font-display text-4xl font-bold text-white">{team?.name ?? "Unavailable"}</p>
            </div>
            <p className="mt-2 text-sm text-white/70">{team?.gameName}</p>
            {team ? (
              <div className="mt-6">
                <Link to={`/teams/${team.id}`} className="text-sm text-accent">
                  Open team profile
                </Link>
              </div>
            ) : null}
          </Card>
        ))}
        <Card>
          <p className="text-xs uppercase tracking-[0.2em] text-white/75">Series score</p>
          <p className="mt-3 font-display text-6xl font-bold text-white">
            {match.scoreTeam1}:{match.scoreTeam2}
          </p>
          <div className="mt-6 rounded-2xl border border-white/8 bg-white/[0.03] p-4">
            <p className="text-xs uppercase tracking-[0.2em] text-white/30">Tournament</p>
            <p className="mt-2 text-white">{tournamentQuery.data?.name ?? `Tournament #${match.tournamentId}`}</p>
          </div>
        </Card>
      </div>
    </>
  );
}
