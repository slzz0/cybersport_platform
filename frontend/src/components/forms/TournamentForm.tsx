import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { Input } from "@/components/ui/Input";
import { Select } from "@/components/ui/Select";
import type { Game, Tournament, TournamentPayload } from "@/types/entities";

const schema = z
  .object({
    name: z.string().min(2, "Tournament name is required").max(150, "Max 150 chars"),
    startDate: z.string().min(1, "Start date is required"),
    endDate: z.string().min(1, "End date is required"),
    prizePool: z.string().min(2, "Prize pool is required").max(120, "Max 120 chars"),
    gameId: z.coerce.number().positive("Select a game"),
  })
  .refine((values) => new Date(values.endDate) >= new Date(values.startDate), {
    message: "End date must be after start date",
    path: ["endDate"],
  });

interface TournamentFormProps {
  tournament?: Tournament | null;
  games: Game[];
  onSubmit: (payload: TournamentPayload) => void;
  formId: string;
}

export function TournamentForm({
  tournament,
  games,
  onSubmit,
  formId,
}: TournamentFormProps) {
  const form = useForm<TournamentPayload>({
    resolver: zodResolver(schema),
    defaultValues: {
      name: tournament?.name ?? "",
      startDate: tournament?.startDate ?? "",
      endDate: tournament?.endDate ?? "",
      prizePool: tournament?.prizePool ?? "$50,000",
      gameId: tournament?.gameId ?? games[0]?.id ?? 0,
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
        label="Tournament name"
        className="md:col-span-2"
        placeholder="GRACEIT Masters"
        error={errors.name?.message}
        {...register("name")}
      />
      <Input label="Start date" type="date" error={errors.startDate?.message} {...register("startDate")} />
      <Input label="End date" type="date" error={errors.endDate?.message} {...register("endDate")} />
      <Input label="Prize pool" error={errors.prizePool?.message} {...register("prizePool")} />
      <Select
        label="Game discipline"
        options={games.map((game) => ({ label: game.name, value: game.id }))}
        error={errors.gameId?.message}
        {...register("gameId")}
      />
    </form>
  );
}
