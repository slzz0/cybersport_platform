import { Link } from "react-router-dom";
import { ArrowRight, ChevronRight, Shield, Swords, Trophy, Users } from "lucide-react";
import { Card } from "@/components/ui/Card";
import { Button } from "@/components/ui/Button";
import { Badge } from "@/components/ui/Badge";
import { TeamAvatar } from "@/components/teams/TeamAvatar";
import { SectionHeading } from "@/components/ui/SectionHeading";
import { SkeletonGrid } from "@/components/data/SkeletonGrid";
import { ErrorState } from "@/components/data/ErrorState";
import { useDashboardData } from "@/features/dashboard/hooks";
import { enrichMatches } from "@/features/matches/utils";
import { enrichTeams } from "@/features/teams/utils";
import { enrichTournaments } from "@/features/tournaments/utils";
import { formatDate, formatDateTime, getMatchStatus, getTournamentStatus } from "@/lib/format";

export function HomePage() {
  const { data, isLoading, isError, refetch } = useDashboardData();

  if (isLoading) {
    return <SkeletonGrid count={6} />;
  }

  if (isError || !data) {
    return (
      <ErrorState
        description="GRACEIT could not load the home surface right now."
        action={<Button onClick={() => refetch()}>Retry</Button>}
      />
    );
  }

  const teams = enrichTeams(data.teams, data.games);
  const tournaments = enrichTournaments(data.tournaments, data.games, data.matches);
  const matches = enrichMatches(data.matches, teams, data.tournaments);
  const topPlayers = data.players.slice().sort((left, right) => right.elo - left.elo).slice(0, 3);
  const featuredGames = data.games.slice(0, 4);
  return (
    <>
      <Card className="hero-line relative overflow-hidden border-accent/15 bg-hero-radial p-7 md:p-8">
        <div className="grid gap-6 xl:grid-cols-[1.15fr_0.85fr]">
          <div>
            <Badge tone="accent">Competitive gaming platform</Badge>
            <h2 className="mt-5 max-w-3xl font-display text-5xl font-bold leading-none tracking-wide text-white md:text-[78px]">
              Queue players, track events, control the circuit.
            </h2>
            <p className="mt-5 max-w-2xl text-base text-white/60 md:text-lg">
              GRACEIT brings match flow, rankings, teams and tournaments into one tighter command surface inspired by
              real competitive platforms.
            </p>
            <div className="mt-6 flex flex-wrap gap-2">
              {featuredGames.map((game) => (
                <span
                  key={game.id}
                  className="inline-flex items-center rounded-full border border-white/10 bg-white/[0.04] px-3 py-1 text-xs font-semibold uppercase tracking-[0.2em] text-white/85"
                >
                  {game.name}
                </span>
              ))}
            </div>
            <div className="mt-8 flex flex-wrap gap-3">
              <Link
                to="/matches"
                className="inline-flex h-11 items-center justify-center gap-2 rounded-[18px] border border-accent/40 bg-accent px-4 text-sm font-semibold text-white shadow-glow transition hover:bg-[#ff8145]"
              >
                Open matches
                <ArrowRight className="h-4 w-4" />
              </Link>
              <Link
                to="/rank"
                className="inline-flex h-11 items-center justify-center gap-2 rounded-[18px] border border-white/10 bg-[#141920] px-4 text-sm font-semibold text-white transition hover:bg-[#191f27]"
              >
                Open rank
                <ChevronRight className="h-4 w-4" />
              </Link>
            </div>
          </div>

          <div className="grid gap-4">
            <div className="grid gap-4 sm:grid-cols-2">
              {[
                { label: "Active players", value: data.players.length, icon: Users },
                { label: "Registered teams", value: data.teams.length, icon: Shield },
                { label: "Matches tracked", value: data.matches.length, icon: Swords },
                { label: "Tournament cycles", value: data.tournaments.length, icon: Trophy },
              ].map((item) => {
                const Icon = item.icon;
                return (
                  <div
                    key={item.label}
                    className="rounded-[18px] border border-white/10 bg-[#11161d] p-4 transition hover:border-accent/20"
                  >
                    <div className="mb-5 flex items-center justify-between">
                      <p className="text-xs uppercase tracking-[0.26em] text-white/85">{item.label}</p>
                      <Icon className="h-4 w-4 text-accent" />
                    </div>
                    <p className="font-display text-4xl font-bold tracking-wide text-white">{item.value}</p>
                  </div>
                );
              })}
            </div>

            <div className="rounded-[20px] border border-white/10 bg-[#11161d] p-5">
              <p className="text-[11px] uppercase tracking-[0.28em] text-white/85">Platform focus</p>
              <h3 className="mt-3 font-display text-3xl font-bold text-white">One cleaner surface for teams, ranks and tournaments.</h3>
              <p className="mt-3 max-w-xl text-sm text-white/52">
                The home lane stays lighter now: quick entry points, visible platform stats and less visual noise before
                diving into matches or tournament pages.
              </p>
              <div className="mt-6 grid gap-3 sm:grid-cols-2">
                <Link
                  to="/teams"
                  className="rounded-[16px] border border-white/8 bg-[#141920] px-4 py-4 transition hover:border-white/14 hover:bg-[#191f27]"
                >
                  <p className="text-xs uppercase tracking-[0.22em] text-white/30">Teams</p>
                  <p className="mt-2 text-lg font-semibold text-white">Manage squads</p>
                </Link>
                <Link
                  to="/tournaments"
                  className="rounded-[16px] border border-white/8 bg-[#141920] px-4 py-4 transition hover:border-white/14 hover:bg-[#191f27]"
                >
                  <p className="text-xs uppercase tracking-[0.22em] text-white/30">Tournaments</p>
                  <p className="mt-2 text-lg font-semibold text-white">Open event tracks</p>
                </Link>
              </div>
            </div>
          </div>
        </div>
      </Card>

      <section className="grid gap-6 xl:grid-cols-3">
        <div className="xl:col-span-3">
          <SectionHeading
            eyebrow="Front page surfaces"
            title="Play, rank and event surfaces"
            description="A denser front page inspired by queue-based competitive platforms and tournament hubs."
          />
        </div>

        <Card>
          <div className="mb-5 flex items-center justify-between">
            <h3 className="font-display text-2xl font-bold text-white">Popular tournaments</h3>
            <Link to="/tournaments" className="text-sm text-accent">
              View all
            </Link>
          </div>
          <div className="space-y-3">
            {tournaments.slice(0, 3).map((tournament) => (
              <Link
                key={tournament.id}
                to={`/tournaments/${tournament.id}`}
                className="block rounded-[16px] border border-white/8 bg-[#141920] p-4 transition hover:border-accent/20 hover:bg-[#191f27]"
              >
                <div className="flex items-start justify-between gap-4">
                  <div>
                    <p className="font-semibold text-white">{tournament.name}</p>
                    <p className="mt-1 text-sm text-white/85">{tournament.gameName}</p>
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
                <div className="mt-4 flex items-center justify-between text-sm text-white/65">
                  <span>{formatDate(tournament.startDate)}</span>
                  <span>{tournament.prizePool}</span>
                </div>
              </Link>
            ))}
          </div>
        </Card>

        <Card>
          <div className="mb-5 flex items-center justify-between">
            <h3 className="font-display text-2xl font-bold text-white">Top teams</h3>
            <Link to="/teams" className="text-sm text-accent">
              View all
            </Link>
          </div>
          <div className="space-y-3">
            {teams.slice(0, 3).map((team) => (
              <Link
                key={team.id}
                to={`/teams/${team.id}`}
                className="flex items-center justify-between rounded-[16px] border border-white/8 bg-[#141920] p-4 transition hover:border-white/15 hover:bg-[#191f27]"
              >
                <div className="flex items-center gap-3">
                  <TeamAvatar name={team.name} gameName={team.gameName} size="md" />
                  <div>
                    <p className="font-semibold text-white">{team.name}</p>
                    <p className="mt-1 text-sm text-white/85">{team.gameName}</p>
                  </div>
                </div>
                <span className="text-sm text-white/85">#{team.id}</span>
              </Link>
            ))}
          </div>
        </Card>

        <Card>
          <div className="mb-5 flex items-center justify-between">
            <h3 className="font-display text-2xl font-bold text-white">Next matches</h3>
            <Link to="/matches" className="text-sm text-accent">
              View all
            </Link>
          </div>
          <div className="space-y-3">
            {matches.slice(0, 3).map((match) => (
              <Link
                key={match.id}
                to={`/matches/${match.id}`}
                className="block rounded-[16px] border border-white/8 bg-[#141920] p-4 transition hover:border-accent/20 hover:bg-[#191f27]"
              >
                <div className="flex items-center justify-between gap-3">
                  <div className="flex items-center gap-3">
                    <TeamAvatar name={match.team1Name} size="sm" />
                    <span className="text-sm font-semibold text-white">{match.team1Name}</span>
                    <span className="text-xs uppercase tracking-[0.18em] text-white/28">vs</span>
                    <span className="text-sm font-semibold text-white">{match.team2Name}</span>
                    <TeamAvatar name={match.team2Name} size="sm" />
                  </div>
                  <Badge tone={getMatchStatus(match) === "Finished" ? "neutral" : "accent"}>
                    {getMatchStatus(match)}
                  </Badge>
                </div>
                <p className="mt-2 text-sm text-white/85">{match.tournamentName}</p>
                <p className="mt-4 text-sm text-white/85">{formatDateTime(match.playedAt)}</p>
              </Link>
            ))}
          </div>
        </Card>
      </section>

      <section className="grid gap-6 lg:grid-cols-[1.1fr_0.9fr]">
        <Card tone="accent">
          <p className="text-[11px] uppercase tracking-[0.32em] text-accent/80">Rank spotlight</p>
          <h3 className="mt-4 font-display text-4xl font-bold tracking-wide text-white">High-ELO player lane</h3>
          <div className="mt-6 grid gap-4">
            {topPlayers.map((player, index) => (
              <div
                key={player.id}
                className="flex items-center justify-between rounded-[16px] border border-white/8 bg-black/20 px-4 py-4"
              >
                <div>
                  <p className="text-xs uppercase tracking-[0.26em] text-white/30">rank {index + 1}</p>
                  <p className="mt-1 text-lg font-semibold text-white">{player.nickname}</p>
                  <p className="text-sm text-white/65">{player.teamName ?? "Unassigned"}</p>
                </div>
                <div className="text-right">
                  <p className="font-display text-3xl font-bold text-white">{player.elo}</p>
                  <p className="text-xs uppercase tracking-[0.2em] text-white/30">elo</p>
                </div>
              </div>
            ))}
          </div>
        </Card>

        <Card className="panel-grid bg-surface/40">
          <p className="text-[11px] uppercase tracking-[0.32em] text-white/85">Platform lanes</p>
          <h3 className="mt-4 font-display text-4xl font-bold tracking-wide text-white">
            Built for queues, squads and event flow.
          </h3>
          <div className="mt-6 grid gap-4">
            {[
              "A denser shell with clearer competitive rhythm and fewer repeated controls.",
              "Reusable CRUD architecture with React Query, Axios and typed API clients.",
              "A cleaner match-platform feel across desktop, tablet and mobile.",
            ].map((item) => (
              <div key={item} className="rounded-[16px] border border-white/8 bg-[#141920] px-4 py-4 text-sm text-white/65">
                {item}
              </div>
            ))}
          </div>
        </Card>
      </section>
    </>
  );
}
