import { Link } from "react-router-dom";
import { useDeferredValue, useState } from "react";
import { Search } from "lucide-react";
import { Badge } from "@/components/ui/Badge";
import { Button } from "@/components/ui/Button";
import { TeamAvatar } from "@/components/teams/TeamAvatar";
import { EmptyState } from "@/components/data/EmptyState";
import { ErrorState } from "@/components/data/ErrorState";
import { GlobalSearchPanel } from "@/components/search/GlobalSearchPanel";
import { SearchGroup } from "@/components/search/SearchGroup";
import { SkeletonGrid } from "@/components/data/SkeletonGrid";
import { useDashboardData } from "@/features/dashboard/hooks";
import { enrichMatches } from "@/features/matches/utils";
import { enrichPlayers } from "@/features/players/utils";
import { enrichTeams } from "@/features/teams/utils";
import { enrichTournaments } from "@/features/tournaments/utils";
import { formatDateTime, getMatchStatus, getTournamentStatus } from "@/lib/format";
import { buildSearchGroups } from "@/services/mappers/search";

export function SearchPage() {
  const [query, setQuery] = useState("");
  const [activeTab, setActiveTab] = useState("all");
  const deferredQuery = useDeferredValue(query);
  const { data, isLoading, isError, refetch } = useDashboardData();

  if (isLoading) {
    return <SkeletonGrid count={4} />;
  }

  if (isError || !data) {
    return (
      <ErrorState
        description="Search is temporarily unavailable."
        action={<Button onClick={() => refetch()}>Retry</Button>}
      />
    );
  }

  const teams = enrichTeams(data.teams, data.games);
  const players = enrichPlayers(data.players, teams);
  const matches = enrichMatches(data.matches, teams, data.tournaments);
  const tournaments = enrichTournaments(data.tournaments, data.games, data.matches);
  const groups = buildSearchGroups({
    query: deferredQuery,
    players,
    teams,
    matches,
    tournaments,
  });
  const visibleGroups = groups.filter((group) => activeTab === "all" || group.type === activeTab);

  return (
    <>
      <div className="grid gap-6 xl:grid-cols-[320px_minmax(0,1fr)]">
        <GlobalSearchPanel
          value={query}
          onChange={setQuery}
          activeTab={activeTab}
          onTabChange={setActiveTab}
        />

        {!deferredQuery.trim() ? (
          <div className="rounded-[30px] border border-white/8 bg-[#0c1014] p-6">
            <div className="mb-5 flex items-center gap-3">
              <div className="flex h-11 w-11 items-center justify-center rounded-2xl bg-white/[0.05]">
                <Search className="h-5 w-5 text-accent" />
              </div>
              <div>
                <p className="font-display text-3xl font-bold tracking-wide text-white">Discover the ecosystem</p>
                <p className="text-sm text-white/45">Use one focused search rail to scan across every core entity.</p>
              </div>
            </div>
            <EmptyState
              title="Start typing to search"
              description="GRACEIT will group results by players, teams, matches and tournaments."
            />
          </div>
        ) : visibleGroups.length === 0 ? (
          <EmptyState
            title="No results matched that query"
            description="Try a player nickname, team name, tournament title or game discipline."
          />
        ) : (
          <div className="grid gap-6">
            {visibleGroups.map((group, index) => (
              <SearchGroup
                key={group.title}
                title={group.title}
                count={group.items.length}
                accent={index === 0}
              >
                {group.type === "players"
                  ? (group.items as typeof players).map((player) => (
                      <Link
                        key={player.id}
                        to={`/players/${player.id}`}
                        className="flex items-center justify-between rounded-2xl border border-white/8 bg-white/[0.025] p-4 transition hover:border-accent/18 hover:bg-white/[0.05]"
                      >
                        <div>
                          <p className="font-semibold text-white">{player.nickname}</p>
                          <p className="mt-1 text-sm text-white/50">{player.teamName}</p>
                        </div>
                        <Badge tone="accent">{player.elo} ELO</Badge>
                      </Link>
                    ))
                  : null}

                {group.type === "teams"
                  ? (group.items as typeof teams).map((team) => (
                      <Link
                        key={team.id}
                        to={`/teams/${team.id}`}
                        className="flex items-center justify-between rounded-2xl border border-white/8 bg-white/[0.025] p-4 transition hover:border-accent/18 hover:bg-white/[0.05]"
                      >
                        <div className="flex items-center gap-3">
                          <TeamAvatar name={team.name} gameName={team.gameName} size="md" />
                          <div>
                            <p className="font-semibold text-white">{team.name}</p>
                            <p className="mt-1 text-sm text-white/50">{team.gameName}</p>
                          </div>
                        </div>
                        <span className="text-sm text-white/35">#{team.id}</span>
                      </Link>
                    ))
                  : null}

                {group.type === "matches"
                  ? (group.items as typeof matches).map((match) => (
                      <Link
                        key={match.id}
                        to={`/matches/${match.id}`}
                        className="flex items-center justify-between rounded-2xl border border-white/8 bg-white/[0.025] p-4 transition hover:border-accent/18 hover:bg-white/[0.05]"
                      >
                        <div>
                          <div className="flex items-center gap-2">
                            <TeamAvatar name={match.team1Name} size="sm" />
                            <p className="font-semibold text-white">{match.team1Name}</p>
                            <span className="text-xs uppercase tracking-[0.18em] text-white/30">vs</span>
                            <p className="font-semibold text-white">{match.team2Name}</p>
                            <TeamAvatar name={match.team2Name} size="sm" />
                          </div>
                          <p className="mt-1 text-sm text-white/50">{match.tournamentName}</p>
                        </div>
                        <div className="text-right">
                          <Badge tone={getMatchStatus(match) === "Finished" ? "neutral" : "accent"}>
                            {getMatchStatus(match)}
                          </Badge>
                          <p className="mt-2 text-xs text-white/35">{formatDateTime(match.playedAt)}</p>
                        </div>
                      </Link>
                    ))
                  : null}

                {group.type === "tournaments"
                  ? (group.items as typeof tournaments).map((tournament) => (
                      <Link
                        key={tournament.id}
                        to={`/tournaments/${tournament.id}`}
                        className="flex items-center justify-between rounded-2xl border border-white/8 bg-white/[0.025] p-4 transition hover:border-accent/18 hover:bg-white/[0.05]"
                      >
                        <div>
                          <p className="font-semibold text-white">{tournament.name}</p>
                          <p className="mt-1 text-sm text-white/50">{tournament.gameName}</p>
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
                      </Link>
                    ))
                  : null}
              </SearchGroup>
            ))}
          </div>
        )}
      </div>
    </>
  );
}
