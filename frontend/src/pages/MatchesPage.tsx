import { Link } from "react-router-dom";
import { useDeferredValue, useEffect, useState } from "react";
import { Pencil, Plus, Trash2 } from "lucide-react";
import { EmptyState } from "@/components/data/EmptyState";
import { ErrorState } from "@/components/data/ErrorState";
import { SkeletonGrid } from "@/components/data/SkeletonGrid";
import { EntityDialog } from "@/components/forms/EntityDialog";
import { MatchForm } from "@/components/forms/MatchForm";
import { TeamAvatar } from "@/components/teams/TeamAvatar";
import { Badge } from "@/components/ui/Badge";
import { Button } from "@/components/ui/Button";
import { Card } from "@/components/ui/Card";
import { Input } from "@/components/ui/Input";
import { Pagination } from "@/components/ui/Pagination";
import { SectionHeading } from "@/components/ui/SectionHeading";
import { Select } from "@/components/ui/Select";
import { useMatchMutations, useMatches } from "@/features/matches/hooks";
import { enrichMatches } from "@/features/matches/utils";
import { useTeams } from "@/features/teams/hooks";
import { useTournaments } from "@/features/tournaments/hooks";
import { useEntityDialog } from "@/hooks/useEntityDialog";
import { useToastStore } from "@/hooks/useToastStore";
import { formatDateTime, getMatchStatus } from "@/lib/format";
import type { Match, MatchPayload } from "@/types/entities";

const formId = "match-form";
const MATCHES_PAGE_SIZE = 6;

export function MatchesPage() {
  const [search, setSearch] = useState("");
  const [statusFilter, setStatusFilter] = useState("");
  const [page, setPage] = useState(1);
  const [deleteTarget, setDeleteTarget] = useState<Match | null>(null);
  const deferredSearch = useDeferredValue(search);

  const matchDialog = useEntityDialog<Match>();
  const { pushToast } = useToastStore();
  const matchesQuery = useMatches();
  const teamsQuery = useTeams();
  const tournamentsQuery = useTournaments();
  const { createMutation, updateMutation, deleteMutation } = useMatchMutations();

  const matches =
    matchesQuery.data && teamsQuery.data && tournamentsQuery.data
      ? enrichMatches(matchesQuery.data, teamsQuery.data, tournamentsQuery.data)
          .filter((match) => {
            const searchLower = deferredSearch.toLowerCase();
            return (
              match.team1Name.toLowerCase().includes(searchLower) ||
              match.team2Name.toLowerCase().includes(searchLower) ||
              match.tournamentName.toLowerCase().includes(searchLower)
            );
          })
          .filter((match) => (statusFilter ? getMatchStatus(match) === statusFilter : true))
      : [];
  const totalPages = Math.max(1, Math.ceil(matches.length / MATCHES_PAGE_SIZE));
  const paginatedMatches = matches.slice((page - 1) * MATCHES_PAGE_SIZE, page * MATCHES_PAGE_SIZE);

  useEffect(() => {
    setPage(1);
  }, [deferredSearch, statusFilter]);

  useEffect(() => {
    if (page > totalPages) {
      setPage(totalPages);
    }
  }, [page, totalPages]);

  if (matchesQuery.isLoading || teamsQuery.isLoading || tournamentsQuery.isLoading) {
    return <SkeletonGrid count={6} />;
  }

  if (
    matchesQuery.isError ||
    teamsQuery.isError ||
    tournamentsQuery.isError ||
    !matchesQuery.data ||
    !teamsQuery.data ||
    !tournamentsQuery.data
  ) {
    return (
      <ErrorState
        description="Matches could not be loaded."
        action={<Button onClick={() => {
          void matchesQuery.refetch();
          void teamsQuery.refetch();
          void tournamentsQuery.refetch();
        }}>Retry</Button>}
      />
    );
  }

  const handleSubmit = async (payload: MatchPayload) => {
    try {
      if (matchDialog.item) {
        await updateMutation.mutateAsync({ id: matchDialog.item.id, payload });
        pushToast({ title: "Match updated", description: "Match data is synced.", tone: "success" });
      } else {
        await createMutation.mutateAsync(payload);
        pushToast({ title: "Match scheduled", description: "New fixture added to the board.", tone: "success" });
      }
      matchDialog.close();
    } catch (error) {
      pushToast({
        title: "Match request failed",
        description: error instanceof Error ? error.message : "Please retry.",
        tone: "danger",
      });
    }
  };

  const handleDelete = async () => {
    if (!deleteTarget) {
      return;
    }

    try {
      await deleteMutation.mutateAsync(deleteTarget.id);
      pushToast({ title: "Match removed", description: "Fixture deleted successfully.", tone: "success" });
      setDeleteTarget(null);
    } catch (error) {
      pushToast({
        title: "Delete failed",
        description: error instanceof Error ? error.message : "Please retry.",
        tone: "danger",
      });
    }
  };

  return (
    <>
      <SectionHeading
        eyebrow="Matches management"
        title="Schedule and results lane"
        description="Track fixtures with tournament context, participants, timestamps and status-driven filtering."
        action={<Button onClick={matchDialog.openCreate}><Plus className="h-4 w-4" />Create match</Button>}
      />

      <Card className="grid gap-4 lg:grid-cols-3">
        <Input label="Search" value={search} onChange={(event) => setSearch(event.target.value)} placeholder="Team or tournament" />
        <Select
          label="Status"
          placeholder="All statuses"
          value={statusFilter}
          onChange={(event) => setStatusFilter(event.target.value)}
          options={[
            { label: "Upcoming", value: "Upcoming" },
            { label: "Live", value: "Live" },
            { label: "Finished", value: "Finished" },
          ]}
        />
        <div className="flex items-end">
          <Button variant="secondary" className="w-full" onClick={() => {
            setSearch("");
            setStatusFilter("");
          }}>
            Reset filters
          </Button>
        </div>
      </Card>

      {!matches.length ? (
        <EmptyState
          title="No matches matched these filters"
          description="Reset the current filters or schedule a new match."
          action={<Button onClick={matchDialog.openCreate}>Add match</Button>}
        />
      ) : (
        <div className="space-y-4">
          <div className="grid gap-5 xl:grid-cols-2">
            {paginatedMatches.map((match) => (
              <Card key={match.id} className="group">
                <div className="flex items-start justify-between gap-4">
                  <div>
                    <p className="text-[11px] uppercase tracking-[0.28em] text-white/85">{match.tournamentName}</p>
                    <Link to={`/matches/${match.id}`} className="mt-3 block font-display text-3xl font-bold tracking-wide text-white transition group-hover:text-accent">
                      {match.team1Name} vs {match.team2Name}
                    </Link>
                  </div>
                  <Badge tone={getMatchStatus(match) === "Finished" ? "neutral" : "accent"}>{getMatchStatus(match)}</Badge>
                </div>
                <div className="matchup-divider mt-6 grid grid-cols-[1fr_auto_1fr] items-center gap-3 rounded-[22px] border border-white/8 bg-white/[0.025] px-4 py-5">
                  <div className="flex items-center gap-3">
                    <TeamAvatar name={match.team1Name} size="md" />
                    <div>
                      <p className="text-xs font-semibold uppercase tracking-[0.2em] text-white">Team 1</p>
                      <p className="mt-1 font-semibold text-white">{match.team1Name}</p>
                    </div>
                  </div>
                  <div className="h-12 w-12" />
                  <div className="flex items-center justify-end gap-3 text-right">
                    <div>
                      <p className="text-xs font-semibold uppercase tracking-[0.2em] text-white">Team 2</p>
                      <p className="mt-1 font-semibold text-white">{match.team2Name}</p>
                    </div>
                    <TeamAvatar name={match.team2Name} size="md" />
                  </div>
                </div>
                <div className="mt-4 grid grid-cols-2 gap-4">
                  <div className="rounded-2xl border border-white/8 bg-white/[0.03] p-4">
                    <p className="text-xs font-semibold uppercase tracking-[0.2em] text-white">Score</p>
                    <p className="mt-2 font-display text-3xl text-white">
                      {match.scoreTeam1}:{match.scoreTeam2}
                    </p>
                  </div>
                  <div className="rounded-2xl border border-white/8 bg-white/[0.03] p-4">
                    <p className="text-xs font-semibold uppercase tracking-[0.2em] text-white">Played at</p>
                    <p className="mt-2 text-sm text-white">{formatDateTime(match.playedAt)}</p>
                  </div>
                </div>
                <div className="mt-6 flex gap-2">
                  <Button variant="secondary" className="flex-1" onClick={() => matchDialog.openEdit(match)}>
                    <Pencil className="h-4 w-4" />
                    Edit
                  </Button>
                  <Button variant="ghost" className="px-4 text-danger hover:text-danger" onClick={() => setDeleteTarget(match)}>
                    <Trash2 className="h-4 w-4" />
                  </Button>
                </div>
              </Card>
            ))}
          </div>
          <Pagination
            page={page}
            totalPages={totalPages}
            totalItems={matches.length}
            pageSize={MATCHES_PAGE_SIZE}
            onPageChange={setPage}
          />
        </div>
      )}

      <EntityDialog
        open={matchDialog.isOpen}
        onClose={matchDialog.close}
        title={matchDialog.item ? "Edit match" : "Create match"}
        description="Match setup is tournament-linked, typed and ready for backend synchronization."
        confirmLabel={matchDialog.item ? "Save changes" : "Create match"}
        submitting={createMutation.isPending || updateMutation.isPending}
        onSubmit={() => (document.getElementById(formId) as HTMLFormElement | null)?.requestSubmit()}
      >
        <MatchForm
          formId={formId}
          match={matchDialog.item}
          tournaments={tournamentsQuery.data}
          teams={teamsQuery.data}
          onSubmit={(payload) => void handleSubmit(payload)}
        />
      </EntityDialog>

      <EntityDialog
        open={Boolean(deleteTarget)}
        onClose={() => setDeleteTarget(null)}
        title="Delete match"
        description="Delete this fixture from the schedule?"
        confirmLabel="Delete match"
        submitting={deleteMutation.isPending}
        onSubmit={() => void handleDelete()}
      >
        <div className="rounded-2xl border border-danger/20 bg-danger/10 p-4 text-sm text-white/85">
          Match history and result context for this entry will be removed.
        </div>
      </EntityDialog>
    </>
  );
}
