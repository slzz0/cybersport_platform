import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { Input } from "@/components/ui/Input";
import { Select } from "@/components/ui/Select";
import type { Game, Team, TeamPayload } from "@/types/entities";

const schema = z.object({
  name: z.string().min(2, "Team name is required").max(120, "Max 120 chars"),
  gameId: z.coerce.number().positive("Select a game"),
});

interface TeamFormProps {
  team?: Team | null;
  games: Game[];
  onSubmit: (payload: TeamPayload) => void;
  formId: string;
}

export function TeamForm({ team, games, onSubmit, formId }: TeamFormProps) {
  const form = useForm<TeamPayload>({
    resolver: zodResolver(schema),
    defaultValues: {
      name: team?.name ?? "",
      gameId: team?.gameId ?? games[0]?.id ?? 0,
    },
  });

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = form;

  return (
    <form id={formId} className="grid gap-4" onSubmit={handleSubmit(onSubmit)}>
      <Input label="Team name" placeholder="Vertex Reign" error={errors.name?.message} {...register("name")} />
      <Select
        label="Game discipline"
        options={games.map((game) => ({ label: game.name, value: game.id }))}
        error={errors.gameId?.message}
        {...register("gameId")}
      />
    </form>
  );
}

