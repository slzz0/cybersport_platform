import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { queryKeys } from "@/lib/query-keys";
import { tournamentsApi } from "@/services/api/tournamentsApi";
import type { TournamentPayload } from "@/types/entities";

export function useTournaments() {
  return useQuery({
    queryKey: queryKeys.tournaments,
    queryFn: tournamentsApi.getAll,
  });
}

export function useTournament(id: number) {
  return useQuery({
    queryKey: queryKeys.tournament(id),
    queryFn: () => tournamentsApi.getById(id),
    enabled: id > 0,
  });
}

export function useTournamentMutations() {
  const queryClient = useQueryClient();

  const invalidate = async () => {
    await Promise.all([
      queryClient.invalidateQueries({ queryKey: queryKeys.tournaments }),
      queryClient.invalidateQueries({ queryKey: queryKeys.matches }),
      queryClient.invalidateQueries({ queryKey: queryKeys.dashboard }),
    ]);
  };

  const createMutation = useMutation({
    mutationFn: (payload: TournamentPayload) => tournamentsApi.create(payload),
    onSuccess: invalidate,
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, payload }: { id: number; payload: TournamentPayload }) =>
      tournamentsApi.update(id, payload),
    onSuccess: invalidate,
  });

  const deleteMutation = useMutation({
    mutationFn: (id: number) => tournamentsApi.delete(id),
    onSuccess: invalidate,
  });

  return { createMutation, updateMutation, deleteMutation };
}
