import { Link } from "react-router-dom";
import { useDeferredValue, useState } from "react";
import { Pencil, Plus, Trash2 } from "lucide-react";
import { DataTable } from "@/components/data/DataTable";
import { EmptyState } from "@/components/data/EmptyState";
import { ErrorState } from "@/components/data/ErrorState";
import { SkeletonGrid } from "@/components/data/SkeletonGrid";
import { EntityDialog } from "@/components/forms/EntityDialog";
import { PlayerForm } from "@/components/forms/PlayerForm";
import { Badge } from "@/components/ui/Badge";
import { Button } from "@/components/ui/Button";
import { Card } from "@/components/ui/Card";
import { Input } from "@/components/ui/Input";
import { SectionHeading } from "@/components/ui/SectionHeading";
import { Select } from "@/components/ui/Select";
import { usePlayerMutations, usePlayers } from "@/features/players/hooks";
import { enrichPlayers } from "@/features/players/utils";
import { useTeams } from "@/features/teams/hooks";
import { useEntityDialog } from "@/hooks/useEntityDialog";
import { useToastStore } from "@/hooks/useToastStore";
import type { Player, PlayerPayload } from "@/types/entities";

const formId = "player-form";

export function PlayersPage() {
  const [search, setSearch] = useState("");
  const [teamFilter, setTeamFilter] = useState("");
  const [minElo, setMinElo] = useState("");
  const [deleteTarget, setDeleteTarget] = useState<Player | null>(null);
  const deferredSearch = useDeferredValue(search);

  const playerDialog = useEntityDialog<Player>();
  const { pushToast } = useToastStore();
  const playersQuery = usePlayers();
  const teamsQuery = useTeams();
  const { createMutation, updateMutation, deleteMutation } = usePlayerMutations();

  if (playersQuery.isLoading || teamsQuery.isLoading) {
    return <SkeletonGrid count={6} />;
  }

  if (playersQuery.isError || teamsQuery.isError || !playersQuery.data || !teamsQuery.data) {
    return (
      <ErrorState
        description="Players could not be loaded."
        action={
          <Button onClick={() => {
            void playersQuery.refetch();
            void teamsQuery.refetch();
          }}>
            Retry
          </Button>
        }
      />
    );
  }

  const players = enrichPlayers(playersQuery.data, teamsQuery.data)
    .filter((player) => player.nickname.toLowerCase().includes(deferredSearch.toLowerCase()))
    .filter((player) => (teamFilter ? `${player.teamId}` === teamFilter : true))
    .filter((player) => (minElo ? player.elo >= Number(minElo) : true));

  const handleSubmit = async (payload: PlayerPayload) => {
    try {
      if (playerDialog.item) {
        await updateMutation.mutateAsync({ id: playerDialog.item.id, payload });
        pushToast({ title: "Player updated", description: `${payload.nickname} is synced.`, tone: "success" });
      } else {
        await createMutation.mutateAsync(payload);
        pushToast({ title: "Player created", description: `${payload.nickname} joined GRACEIT.`, tone: "success" });
      }
      playerDialog.close();
    } catch (error) {
      pushToast({
        title: "Player request failed",
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
      pushToast({ title: "Player removed", description: `${deleteTarget.nickname} was deleted.`, tone: "success" });
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
        eyebrow="Players management"
        title="Roster talent control"
        description="Search, filter and maintain the player pool with fast CRUD actions and direct access to detailed profiles."
        action={<Button onClick={playerDialog.openCreate}><Plus className="h-4 w-4" />Create player</Button>}
      />

      <Card className="grid gap-4 lg:grid-cols-4">
        <Input label="Search" value={search} onChange={(event) => setSearch(event.target.value)} placeholder="Search by nickname" />
        <Select
          label="Team"
          placeholder="All teams"
          value={teamFilter}
          onChange={(event) => setTeamFilter(event.target.value)}
          options={teamsQuery.data.map((team) => ({ label: team.name, value: team.id }))}
        />
        <Input
          label="Min ELO"
          type="number"
          min={0}
          value={minElo}
          onChange={(event) => setMinElo(event.target.value)}
          placeholder="2200"
        />
        <div className="flex items-end">
          <Button variant="secondary" className="w-full" onClick={() => {
            setSearch("");
            setTeamFilter("");
            setMinElo("");
          }}>
            Reset filters
          </Button>
        </div>
      </Card>

      {!players.length ? (
        <EmptyState
          title="No players matched these filters"
          description="Adjust the search or create a new player entry."
          action={<Button onClick={playerDialog.openCreate}>Add player</Button>}
        />
      ) : (
        <DataTable<Player>
          data={players}
          columns={[
            {
              key: "nickname",
              header: "Player",
              render: (player) => (
                <div>
                  <Link to={`/players/${player.id}`} className="font-semibold text-white hover:text-accent">
                    {player.nickname}
                  </Link>
                  <p className="mt-1 text-xs text-white/40">ID #{player.id}</p>
                </div>
              ),
            },
            {
              key: "team",
              header: "Team",
              render: (player) => <span>{player.teamName}</span>,
            },
            {
              key: "elo",
              header: "ELO",
              render: (player) => <Badge tone="accent">{player.elo}</Badge>,
            },
            {
              key: "actions",
              header: "Actions",
              className: "w-[180px]",
              render: (player) => (
                <div className="flex gap-2">
                  <Button variant="ghost" className="h-10 px-3" onClick={() => playerDialog.openEdit(player)}>
                    <Pencil className="h-4 w-4" />
                  </Button>
                  <Button variant="ghost" className="h-10 px-3 text-danger hover:text-danger" onClick={() => setDeleteTarget(player)}>
                    <Trash2 className="h-4 w-4" />
                  </Button>
                </div>
              ),
            },
          ]}
        />
      )}

      <EntityDialog
        open={playerDialog.isOpen}
        onClose={playerDialog.close}
        title={playerDialog.item ? "Edit player" : "Create player"}
        description="Keep the roster surface accurate with typed validation and clean team assignment."
        confirmLabel={playerDialog.item ? "Save changes" : "Create player"}
        submitting={createMutation.isPending || updateMutation.isPending}
        onSubmit={() => (document.getElementById(formId) as HTMLFormElement | null)?.requestSubmit()}
      >
        <PlayerForm
          formId={formId}
          player={playerDialog.item}
          teams={teamsQuery.data}
          onSubmit={(payload) => void handleSubmit(payload)}
        />
      </EntityDialog>

      <EntityDialog
        open={Boolean(deleteTarget)}
        onClose={() => setDeleteTarget(null)}
        title="Delete player"
        description={`Remove ${deleteTarget?.nickname ?? "this player"} from the platform? This action cannot be undone.`}
        confirmLabel="Delete player"
        submitting={deleteMutation.isPending}
        onSubmit={() => void handleDelete()}
      >
        <div className="rounded-2xl border border-danger/20 bg-danger/10 p-4 text-sm text-white/85">
          Player details and related views will no longer be accessible after deletion.
        </div>
      </EntityDialog>
    </>
  );
}
