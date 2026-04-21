import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { Input } from "@/components/ui/Input";
import { Select } from "@/components/ui/Select";
import { formatInputDateTime } from "@/lib/format";
import type { Match, MatchPayload, Team, Tournament } from "@/types/entities";

const schema = z
  .object({
    tournamentId: z.coerce.number().positive("Select a tournament"),
    team1Id: z.coerce.number().positive("Select the first team"),
    team2Id: z.coerce.number().positive("Select the second team"),
    scoreTeam1: z.coerce.number().min(0, "Score must be 0 or above"),
    scoreTeam2: z.coerce.number().min(0, "Score must be 0 or above"),
    playedAt: z.string().min(1, "Match date is required"),
  })
  .refine((values) => values.team1Id !== values.team2Id, {
    message: "Teams must be different",
    path: ["team2Id"],
  });

interface MatchFormProps {
  match?: Match | null;
  tournaments: Tournament[];
  teams: Team[];
  onSubmit: (payload: MatchPayload) => void;
  formId: string;
}

export function MatchForm({
  match,
  tournaments,
  teams,
  onSubmit,
  formId,
}: MatchFormProps) {
  const form = useForm<MatchPayload>({
    resolver: zodResolver(schema),
    defaultValues: {
      tournamentId: match?.tournamentId ?? tournaments[0]?.id ?? 0,
      team1Id: match?.team1Id ?? teams[0]?.id ?? 0,
      team2Id: match?.team2Id ?? teams[1]?.id ?? teams[0]?.id ?? 0,
      scoreTeam1: match?.scoreTeam1 ?? 0,
      scoreTeam2: match?.scoreTeam2 ?? 0,
      playedAt: match?.playedAt ? formatInputDateTime(match.playedAt) : "",
    },
  });

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = form;

  return (
    <form id={formId} className="grid gap-4 md:grid-cols-2" onSubmit={handleSubmit(onSubmit)}>
      <Select
        label="Tournament"
        options={tournaments.map((tournament) => ({ label: tournament.name, value: tournament.id }))}
        error={errors.tournamentId?.message}
        className="md:col-span-2"
        {...register("tournamentId")}
      />
      <Select
        label="Team one"
        options={teams.map((team) => ({ label: team.name, value: team.id }))}
        error={errors.team1Id?.message}
        {...register("team1Id")}
      />
      <Select
        label="Team two"
        options={teams.map((team) => ({ label: team.name, value: team.id }))}
        error={errors.team2Id?.message}
        {...register("team2Id")}
      />
      <Input
        label="Score team one"
        type="number"
        min={0}
        error={errors.scoreTeam1?.message}
        {...register("scoreTeam1")}
      />
      <Input
        label="Score team two"
        type="number"
        min={0}
        error={errors.scoreTeam2?.message}
        {...register("scoreTeam2")}
      />
      <Input
        label="Played at"
        type="datetime-local"
        className="md:col-span-2"
        error={errors.playedAt?.message}
        {...register("playedAt")}
      />
    </form>
  );
}

