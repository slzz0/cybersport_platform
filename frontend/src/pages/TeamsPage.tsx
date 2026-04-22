import { Link } from "react-router-dom";
import { useDeferredValue, useState } from "react";
import { Pencil, Plus, Trash2, Users } from "lucide-react";
import { EmptyState } from "@/components/data/EmptyState";
import { ErrorState } from "@/components/data/ErrorState";
import { SkeletonGrid } from "@/components/data/SkeletonGrid";
import { EntityDialog } from "@/components/forms/EntityDialog";
import { TeamForm } from "@/components/forms/TeamForm";
import { TeamAvatar } from "@/components/teams/TeamAvatar";
import { Badge } from "@/components/ui/Badge";
import { Button } from "@/components/ui/Button";
import { Card } from "@/components/ui/Card";
import { Input } from "@/components/ui/Input";
import { SectionHeading } from "@/components/ui/SectionHeading";
import { Select } from "@/components/ui/Select";
import { useGames } from "@/features/games/hooks";
import { usePlayers } from "@/features/players/hooks";
import { useTeams, useTeamMutations } from "@/features/teams/hooks";
import { enrichTeams, getTeamPlayers } from "@/features/teams/utils";
import { useEntityDialog } from "@/hooks/useEntityDialog";
import { useToastStore } from "@/hooks/useToastStore";
import type { Team, TeamPayload } from "@/types/entities";

const formId = "team-form";

export function TeamsPage() {
  const [search, setSearch] = useState("");
  const [gameFilter, setGameFilter] = useState("");
  const [deleteTarget, setDeleteTarget] = useState<Team | null>(null);
  const deferredSearch = useDeferredValue(search);

  const teamDialog = useEntityDialog<Team>();
  const { pushToast } = useToastStore();
  const teamsQuery = useTeams();
  const gamesQuery = useGames();
  const playersQuery = usePlayers();
  const { createMutation, updateMutation, deleteMutation } = useTeamMutations();

  if (teamsQuery.isLoading || gamesQuery.isLoading || playersQuery.isLoading) {
    return <SkeletonGrid count={6} />;
  }

  if (
    teamsQuery.isError ||
    gamesQuery.isError ||
    playersQuery.isError ||
    !teamsQuery.data ||
    !gamesQuery.data ||
    !playersQuery.data
  ) {
    return (
      <ErrorState
        description="Teams could not be loaded."
        action={<Button onClick={() => {
          void teamsQuery.refetch();
          void gamesQuery.refetch();
          void playersQuery.refetch();
        }}>Retry</Button>}
      />
    );
  }

  const teams = enrichTeams(teamsQuery.data, gamesQuery.data)
    .filter((team) => team.name.toLowerCase().includes(deferredSearch.toLowerCase()))
    .filter((team) => (gameFilter ? `${team.gameId}` === gameFilter : true));

  const handleSubmit = async (payload: TeamPayload) => {
    try {
      if (teamDialog.item) {
        await updateMutation.mutateAsync({ id: teamDialog.item.id, payload });
        pushToast({ title: "Team updated", description: `${payload.name} is synced.`, tone: "success" });
      } else {
        await createMutation.mutateAsync(payload);
        pushToast({ title: "Team created", description: `${payload.name} entered the circuit.`, tone: "success" });
      }
      teamDialog.close();
    } catch (error) {
      pushToast({
        title: "Team request failed",
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
      pushToast({ title: "Team removed", description: `${deleteTarget.name} was deleted.`, tone: "success" });
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
        eyebrow="Teams management"
        title="Squads and lineups"
        description="Manage teams with game discipline context and direct visibility into the players assigned to each lineup."
        action={<Button onClick={teamDialog.openCreate}><Plus className="h-4 w-4" />Create team</Button>}
      />

      <Card className="grid gap-4 lg:grid-cols-3">
        <Input label="Search" value={search} onChange={(event) => setSearch(event.target.value)} placeholder="Search by team name" />
        <Select
          label="Game"
          placeholder="All games"
          value={gameFilter}
          onChange={(event) => setGameFilter(event.target.value)}
          options={gamesQuery.data.map((game) => ({ label: game.name, value: game.id }))}
        />
        <div className="flex items-end">
          <Button variant="secondary" className="w-full" onClick={() => {
            setSearch("");
            setGameFilter("");
          }}>
            Reset filters
          </Button>
        </div>
      </Card>

      {!teams.length ? (
        <EmptyState
          title="No teams matched these filters"
          description="Try another search term or create a fresh lineup."
          action={<Button onClick={teamDialog.openCreate}>Add team</Button>}
        />
      ) : (
        <div className="grid gap-5 md:grid-cols-2 xl:grid-cols-3">
          {teams.map((team) => {
            const members = getTeamPlayers(team.id, playersQuery.data);
            return (
              <Card key={team.id} className="group flex h-full flex-col">
                <div className="flex items-start justify-between gap-4">
                  <div className="flex items-start gap-4">
                    <TeamAvatar name={team.name} gameName={team.gameName} size="lg" />
                    <div>
                      <p className="text-[11px] uppercase tracking-[0.28em] text-white/85">{team.gameName}</p>
                      <Link
                        to={`/teams/${team.id}`}
                        className="mt-3 block font-display text-3xl font-bold tracking-wide text-white transition group-hover:text-accent"
                      >
                        {team.name}
                      </Link>
                    </div>
                  </div>
                  <Badge tone="accent">#{team.id}</Badge>
                </div>
                <div className="mt-6 grid gap-4 sm:grid-cols-[1fr_auto]">
                  <div className="rounded-2xl border border-white/8 bg-white/[0.03] p-4">
                    <p className="text-[11px] uppercase tracking-[0.28em] text-white/85">{team.gameName}</p>
                    <p className="mt-2 text-sm text-white/85">Roster presence, discipline context and quick team actions.</p>
                  </div>
                  <div className="rounded-2xl border border-white/8 bg-white/[0.03] px-4 py-4 text-right">
                    <p className="text-xs uppercase tracking-[0.2em] text-white/30">Roster</p>
                    <p className="mt-2 font-display text-3xl text-white">{members.length}</p>
                  </div>
                </div>
                <div className="mt-4 rounded-2xl border border-white/8 bg-white/[0.03] p-4">
                  <div className="mb-4 flex items-center gap-2 text-sm text-white/85">
                    <Users className="h-4 w-4 text-accent" />
                    {members.length} registered players
                  </div>
                  <div className="space-y-2">
                    {members.slice(0, 3).map((player) => (
                      <div key={player.id} className="flex items-center justify-between text-sm text-white/85">
                        <span>{player.nickname}</span>
                        <span className="text-white/85">{player.elo} ELO</span>
                      </div>
                    ))}
                    {!members.length ? <p className="text-sm text-white/85">No players assigned yet.</p> : null}
                  </div>
                </div>
                <div className="mt-6 flex gap-2">
                  <Button variant="secondary" className="flex-1" onClick={() => teamDialog.openEdit(team)}>
                    <Pencil className="h-4 w-4" />
                    Edit
                  </Button>
                  <Button variant="ghost" className="px-4 text-danger hover:text-danger" onClick={() => setDeleteTarget(team)}>
                    <Trash2 className="h-4 w-4" />
                  </Button>
                </div>
              </Card>
            );
          })}
        </div>
      )}

      <EntityDialog
        open={teamDialog.isOpen}
        onClose={teamDialog.close}
        title={teamDialog.item ? "Edit team" : "Create team"}
        description="Teams stay compact, typed and easy to update as the tournament structure evolves."
        confirmLabel={teamDialog.item ? "Save changes" : "Create team"}
        submitting={createMutation.isPending || updateMutation.isPending}
        onSubmit={() => (document.getElementById(formId) as HTMLFormElement | null)?.requestSubmit()}
      >
        <TeamForm
          formId={formId}
          team={teamDialog.item}
          games={gamesQuery.data}
          onSubmit={(payload) => void handleSubmit(payload)}
        />
      </EntityDialog>

      <EntityDialog
        open={Boolean(deleteTarget)}
        onClose={() => setDeleteTarget(null)}
        title="Delete team"
        description={`Remove ${deleteTarget?.name ?? "this team"} from the platform?`}
        confirmLabel="Delete team"
        submitting={deleteMutation.isPending}
        onSubmit={() => void handleDelete()}
      >
        <div className="rounded-2xl border border-danger/20 bg-danger/10 p-4 text-sm text-white/85">
          Players linked to this team may become invalid depending on backend constraints.
        </div>
      </EntityDialog>
    </>
  );
}
