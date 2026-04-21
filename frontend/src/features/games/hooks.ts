import { useQuery } from "@tanstack/react-query";
import { queryKeys } from "@/lib/query-keys";
import { gamesApi } from "@/services/api/gamesApi";

export function useGames() {
  return useQuery({
    queryKey: queryKeys.games,
    queryFn: gamesApi.getAll,
  });
}

