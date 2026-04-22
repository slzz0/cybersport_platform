import { Link } from "react-router-dom";
import { useDeferredValue, useState } from "react";
import { Pencil, Plus, Trash2 } from "lucide-react";
import { EmptyState } from "@/components/data/EmptyState";
import { ErrorState } from "@/components/data/ErrorState";
import { SkeletonGrid } from "@/components/data/SkeletonGrid";
import { EntityDialog } from "@/components/forms/EntityDialog";
import { TournamentForm } from "@/components/forms/TournamentForm";
import { Badge } from "@/components/ui/Badge";
import { Button } from "@/components/ui/Button";
import { Card } from "@/components/ui/Card";
import { Input } from "@/components/ui/Input";
import { SectionHeading } from "@/components/ui/SectionHeading";
import { Select } from "@/components/ui/Select";
import { useGames } from "@/features/games/hooks";
import { useMatches } from "@/features/matches/hooks";
import { useTournamentMutations, useTournaments } from "@/features/tournaments/hooks";
import { enrichTournaments } from "@/features/tournaments/utils";
import { useEntityDialog } from "@/hooks/useEntityDialog";
import { useToastStore } from "@/hooks/useToastStore";
import { formatDate, getTournamentStatus } from "@/lib/format";
import type { Tournament, TournamentPayload } from "@/types/entities";

const formId = "tournament-form";

export function TournamentsPage() {
  const [search, setSearch] = useState("");
  const [statusFilter, setStatusFilter] = useState("");
  const [deleteTarget, setDeleteTarget] = useState<Tournament | null>(null);
  const deferredSearch = useDeferredValue(search);

  const tournamentDialog = useEntityDialog<Tournament>();
  const { pushToast } = useToastStore();
  const tournamentsQuery = useTournaments();
  const matchesQuery = useMatches();
  const gamesQuery = useGames();
  const { createMutation, updateMutation, deleteMutation } = useTournamentMutations();

  if (tournamentsQuery.isLoading || matchesQuery.isLoading || gamesQuery.isLoading) {
    return <SkeletonGrid count={6} />;
  }

  if (
    tournamentsQuery.isError ||
    matchesQuery.isError ||
    gamesQuery.isError ||
    !tournamentsQuery.data ||
    !matchesQuery.data ||
    !gamesQuery.data
  ) {
    return (
      <ErrorState
        description="Tournaments could not be loaded."
        action={<Button onClick={() => {
          void tournamentsQuery.refetch();
          void matchesQuery.refetch();
          void gamesQuery.refetch();
        }}>Retry</Button>}
      />
    );
  }

  const tournaments = enrichTournaments(tournamentsQuery.data, gamesQuery.data, matchesQuery.data)
    .filter((tournament) => tournament.name.toLowerCase().includes(deferredSearch.toLowerCase()))
    .filter((tournament) => (statusFilter ? getTournamentStatus(tournament) === statusFilter : true));

  const handleSubmit = async (payload: TournamentPayload) => {
    try {
      if (tournamentDialog.item) {
        await updateMutation.mutateAsync({ id: tournamentDialog.item.id, payload });
        pushToast({ title: "Tournament updated", description: `${payload.name} is synced.`, tone: "success" });
      } else {
        await createMutation.mutateAsync(payload);
        pushToast({ title: "Tournament created", description: `${payload.name} joined the calendar.`, tone: "success" });
      }
      tournamentDialog.close();
    } catch (error) {
      pushToast({
        title: "Tournament request failed",
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
      pushToast({ title: "Tournament removed", description: `${deleteTarget.name} was deleted.`, tone: "success" });
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
        eyebrow="Tournaments management"
        title="Event calendar and prize structure"
        description="Keep the full competitive season organized with status-aware tournament cards and quick CRUD flows."
        action={<Button onClick={tournamentDialog.openCreate}><Plus className="h-4 w-4" />Create tournament</Button>}
      />

      <Card className="grid gap-4 lg:grid-cols-3">
        <Input label="Search" value={search} onChange={(event) => setSearch(event.target.value)} placeholder="Tournament name" />
        <Select
          label="Status"
          placeholder="All statuses"
          value={statusFilter}
          onChange={(event) => setStatusFilter(event.target.value)}
          options={[
            { label: "Upcoming", value: "Upcoming" },
            { label: "Active", value: "Active" },
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

      {!tournaments.length ? (
        <EmptyState
          title="No tournaments matched these filters"
          description="Reset the current filters or create a new tournament."
          action={<Button onClick={tournamentDialog.openCreate}>Add tournament</Button>}
        />
      ) : (
        <div className="grid gap-5 md:grid-cols-2 xl:grid-cols-3">
          {tournaments.map((tournament) => (
            <Card key={tournament.id} className="group flex h-full flex-col">
              <div className="flex items-start justify-between gap-4">
                <div>
                  <p className="text-[11px] uppercase tracking-[0.28em] text-white/85">{tournament.gameName}</p>
                  <Link to={`/tournaments/${tournament.id}`} className="mt-3 block font-display text-3xl font-bold tracking-wide text-white transition group-hover:text-accent">
                    {tournament.name}
                  </Link>
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
              <div className="mt-6 grid grid-cols-2 gap-4">
                <div className="rounded-2xl border border-white/8 bg-white/[0.03] p-4">
                  <p className="text-xs font-semibold uppercase tracking-[0.2em] text-white">Window</p>
                  <p className="mt-2 text-sm text-white">
                    {formatDate(tournament.startDate)} - {formatDate(tournament.endDate)}
                  </p>
                </div>
                <div className="rounded-2xl border border-white/8 bg-white/[0.03] p-4">
                  <p className="text-xs font-semibold uppercase tracking-[0.2em] text-white">Matches</p>
                  <p className="mt-2 text-sm text-white">{tournament.matchCount}</p>
                </div>
              </div>
              <div className="mt-4 rounded-2xl border border-white/8 bg-white/[0.03] p-4">
                <p className="text-xs font-semibold uppercase tracking-[0.2em] text-white">Prize pool</p>
                <p className="mt-2 font-display text-2xl text-white">{tournament.prizePool}</p>
              </div>
              <div className="mt-6 flex gap-2">
                <Button variant="secondary" className="flex-1" onClick={() => tournamentDialog.openEdit(tournament)}>
                  <Pencil className="h-4 w-4" />
                  Edit
                </Button>
                <Button variant="ghost" className="px-4 text-danger hover:text-danger" onClick={() => setDeleteTarget(tournament)}>
                  <Trash2 className="h-4 w-4" />
                </Button>
              </div>
            </Card>
          ))}
        </div>
      )}

      <EntityDialog
        open={tournamentDialog.isOpen}
        onClose={tournamentDialog.close}
        title={tournamentDialog.item ? "Edit tournament" : "Create tournament"}
        description="Set the event window, prize pool and game discipline with validation built in."
        confirmLabel={tournamentDialog.item ? "Save changes" : "Create tournament"}
        submitting={createMutation.isPending || updateMutation.isPending}
        onSubmit={() => (document.getElementById(formId) as HTMLFormElement | null)?.requestSubmit()}
      >
        <TournamentForm
          formId={formId}
          tournament={tournamentDialog.item}
          games={gamesQuery.data}
          onSubmit={(payload) => void handleSubmit(payload)}
        />
      </EntityDialog>

      <EntityDialog
        open={Boolean(deleteTarget)}
        onClose={() => setDeleteTarget(null)}
        title="Delete tournament"
        description="Delete this tournament and remove it from the event calendar?"
        confirmLabel="Delete tournament"
        submitting={deleteMutation.isPending}
        onSubmit={() => void handleDelete()}
      >
        <div className="rounded-2xl border border-danger/20 bg-danger/10 p-4 text-sm text-white/85">
          Related matches may no longer have a valid tournament reference after deletion.
        </div>
      </EntityDialog>
    </>
  );
}
