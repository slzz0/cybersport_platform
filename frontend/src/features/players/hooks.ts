import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { queryKeys } from "@/lib/query-keys";
import { playersApi } from "@/services/api/playersApi";
import type { PlayerPayload } from "@/types/entities";

export function usePlayers() {
  return useQuery({
    queryKey: queryKeys.players,
    queryFn: playersApi.getAll,
  });
}

export function usePlayer(id: number) {
  return useQuery({
    queryKey: queryKeys.player(id),
    queryFn: () => playersApi.getById(id),
    enabled: id > 0,
  });
}

export function usePlayerMutations() {
  const queryClient = useQueryClient();

  const invalidate = async () => {
    await Promise.all([
      queryClient.invalidateQueries({ queryKey: queryKeys.players }),
      queryClient.invalidateQueries({ queryKey: queryKeys.dashboard }),
    ]);
  };

  const createMutation = useMutation({
    mutationFn: (payload: PlayerPayload) => playersApi.create(payload),
    onSuccess: invalidate,
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, payload }: { id: number; payload: PlayerPayload }) =>
      playersApi.update(id, payload),
    onSuccess: invalidate,
  });

  const deleteMutation = useMutation({
    mutationFn: (id: number) => playersApi.delete(id),
    onSuccess: invalidate,
  });

  return { createMutation, updateMutation, deleteMutation };
}
