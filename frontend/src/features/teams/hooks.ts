import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { queryKeys } from "@/lib/query-keys";
import { teamsApi } from "@/services/api/teamsApi";
import type { TeamPayload } from "@/types/entities";

export function useTeams() {
  return useQuery({
    queryKey: queryKeys.teams,
    queryFn: teamsApi.getAll,
  });
}

export function useTeam(id: number) {
  return useQuery({
    queryKey: queryKeys.team(id),
    queryFn: () => teamsApi.getById(id),
    enabled: id > 0,
  });
}

export function useTeamMutations() {
  const queryClient = useQueryClient();

  const invalidate = async () => {
    await Promise.all([
      queryClient.invalidateQueries({ queryKey: queryKeys.teams }),
      queryClient.invalidateQueries({ queryKey: queryKeys.players }),
      queryClient.invalidateQueries({ queryKey: queryKeys.dashboard }),
    ]);
  };

  const createMutation = useMutation({
    mutationFn: (payload: TeamPayload) => teamsApi.create(payload),
    onSuccess: invalidate,
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, payload }: { id: number; payload: TeamPayload }) =>
      teamsApi.update(id, payload),
    onSuccess: invalidate,
  });

  const deleteMutation = useMutation({
    mutationFn: (id: number) => teamsApi.delete(id),
    onSuccess: invalidate,
  });

  return { createMutation, updateMutation, deleteMutation };
}
