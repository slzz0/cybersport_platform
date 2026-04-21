import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { Input } from "@/components/ui/Input";
import { Select } from "@/components/ui/Select";
import type { Player, PlayerPayload, Team } from "@/types/entities";

const schema = z.object({
  nickname: z.string().min(2, "Nickname is required").max(80, "Max 80 chars"),
  elo: z.coerce.number().min(0, "ELO must be greater or equal to 0"),
  teamId: z.coerce.number().positive("Select a team"),
});

interface PlayerFormProps {
  player?: Player | null;
  teams: Team[];
  onSubmit: (payload: PlayerPayload) => void;
  formId: string;
}

export function PlayerForm({ player, teams, onSubmit, formId }: PlayerFormProps) {
  const form = useForm<PlayerPayload>({
    resolver: zodResolver(schema),
    defaultValues: {
      nickname: player?.nickname ?? "",
      elo: player?.elo ?? 2000,
      teamId: player?.teamId ?? teams[0]?.id ?? 0,
    },
  });

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = form;

  return (
    <form id={formId} className="grid gap-4 md:grid-cols-2" onSubmit={handleSubmit(onSubmit)}>
      <Input
        label="Nickname"
        placeholder="Riven"
        error={errors.nickname?.message}
        className="md:col-span-2"
        {...register("nickname")}
      />
      <Input
        label="ELO"
        type="number"
        min={0}
        error={errors.elo?.message}
        {...register("elo")}
      />
      <Select
        label="Team"
        options={teams.map((team) => ({ label: team.name, value: team.id }))}
        error={errors.teamId?.message}
        {...register("teamId")}
      />
    </form>
  );
}

