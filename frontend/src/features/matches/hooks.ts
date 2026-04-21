import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { queryKeys } from "@/lib/query-keys";
import { matchesApi } from "@/services/api/matchesApi";
import type { MatchPayload } from "@/types/entities";

export function useMatches() {
  return useQuery({
    queryKey: queryKeys.matches,
    queryFn: matchesApi.getAll,
  });
}

export function useMatch(id: number) {
  return useQuery({
    queryKey: queryKeys.match(id),
    queryFn: () => matchesApi.getById(id),
    enabled: id > 0,
  });
}

export function useMatchMutations() {
  const queryClient = useQueryClient();

  const invalidate = async () => {
    await Promise.all([
      queryClient.invalidateQueries({ queryKey: queryKeys.matches }),
      queryClient.invalidateQueries({ queryKey: queryKeys.dashboard }),
    ]);
  };

  const createMutation = useMutation({
    mutationFn: (payload: MatchPayload) => matchesApi.create(payload),
    onSuccess: invalidate,
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, payload }: { id: number; payload: MatchPayload }) =>
      matchesApi.update(id, payload),
    onSuccess: invalidate,
  });

  const deleteMutation = useMutation({
    mutationFn: (id: number) => matchesApi.delete(id),
    onSuccess: invalidate,
  });

  return { createMutation, updateMutation, deleteMutation };
}
