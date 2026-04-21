import { Activity, Shield, Swords, Trophy, Users } from "lucide-react";
import { Card } from "@/components/ui/Card";
import { Button } from "@/components/ui/Button";
import { SectionHeading } from "@/components/ui/SectionHeading";
import { ErrorState } from "@/components/data/ErrorState";
import { SkeletonGrid } from "@/components/data/SkeletonGrid";
import { QuickActions } from "@/components/dashboard/QuickActions";
import { RecentActivity } from "@/components/dashboard/RecentActivity";
import { StatCard } from "@/components/dashboard/StatCard";
import { useDashboardData } from "@/features/dashboard/hooks";
import { buildDashboardViewModel } from "@/features/dashboard/selectors";
import { formatDateTime, getMatchStatus, getTournamentStatus } from "@/lib/format";
import { Badge } from "@/components/ui/Badge";
import { TeamAvatar } from "@/components/teams/TeamAvatar";

export function DashboardPage() {
  const { data, isLoading, isError, refetch } = useDashboardData();

  if (isLoading) {
    return <SkeletonGrid count={8} />;
  }

  if (isError || !data) {
    return (
      <ErrorState
        description="Dashboard data is unavailable at the moment."
        action={<Button onClick={() => refetch()}>Retry</Button>}
      />
    );
  }

  const viewModel = buildDashboardViewModel(data);
  const icons = [Users, Shield, Swords, Trophy];

  return (
    <>
      <SectionHeading
        eyebrow="Admin section"
        title="Command center"
        description="A high-signal competitive workspace for queue activity, featured matches and tournament operations."
      />

      <section className="grid gap-4 md:grid-cols-2 xl:grid-cols-4">
        {viewModel.stats.map((stat, index) => {
          const Icon = icons[index];
          return <StatCard key={stat.label} label={stat.label} value={stat.value} trend={stat.trend} icon={Icon} />;
        })}
      </section>

      <section className="grid gap-6 xl:grid-cols-[1.05fr_0.95fr]">
        <Card tone="accent">
          <div className="flex items-center justify-between gap-4">
            <div>
              <p className="text-[11px] uppercase tracking-[0.32em] text-accent/80">Spotlight matches</p>
              <h3 className="mt-3 font-display text-3xl font-bold tracking-wide text-white">Today in the arena</h3>
            </div>
            <div className="rounded-[16px] border border-white/10 bg-black/20 p-3">
              <Activity className="h-5 w-5 text-accent" />
            </div>
          </div>
          <div className="mt-6 space-y-4">
            {viewModel.spotlightMatches.map((match) => (
              <div key={match.id} className="rounded-[18px] border border-white/8 bg-black/20 p-5">
                <div className="flex items-center justify-between gap-4">
                  <div>
                    <div className="flex items-center gap-2">
                      <TeamAvatar name={match.team1Name} size="sm" />
                      <p className="font-semibold text-white">{match.team1Name}</p>
                      <span className="text-xs uppercase tracking-[0.22em] text-white/30">vs</span>
                      <p className="font-semibold text-white">{match.team2Name}</p>
                      <TeamAvatar name={match.team2Name} size="sm" />
                    </div>
                    <p className="mt-1 text-sm text-white/50">{match.tournamentName}</p>
                  </div>
                  <Badge tone={getMatchStatus(match) === "Finished" ? "neutral" : "accent"}>
                    {getMatchStatus(match)}
                  </Badge>
                </div>
                <div className="mt-5 flex items-center justify-between text-sm text-white/55">
                  <span>{formatDateTime(match.playedAt)}</span>
                  <span>
                    {match.scoreTeam1}:{match.scoreTeam2}
                  </span>
                </div>
              </div>
            ))}
          </div>
        </Card>

        <div className="grid gap-6">
          <QuickActions />
          <RecentActivity items={viewModel.recentItems} />
        </div>
      </section>

      <section className="grid gap-6 lg:grid-cols-3">
        {viewModel.spotlightTournaments.map((tournament) => (
          <Card key={tournament.id}>
            <div className="flex items-start justify-between gap-4">
              <div>
                <p className="text-[11px] uppercase tracking-[0.28em] text-white/35">Tournament track</p>
                <h3 className="mt-3 font-display text-3xl font-bold text-white">{tournament.name}</h3>
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
            <div className="mt-6 grid grid-cols-2 gap-3">
              <div className="rounded-[16px] border border-white/8 bg-[#141920] p-4">
                <p className="text-xs uppercase tracking-[0.2em] text-white/30">Game</p>
                <p className="mt-2 text-sm text-white">{tournament.gameName}</p>
              </div>
              <div className="rounded-[16px] border border-white/8 bg-[#141920] p-4">
                <p className="text-xs uppercase tracking-[0.2em] text-white/30">Matches</p>
                <p className="mt-2 text-sm text-white">{tournament.matchCount}</p>
              </div>
            </div>
          </Card>
        ))}
      </section>
    </>
  );
}
