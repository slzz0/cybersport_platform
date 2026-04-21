import { useQuery } from "@tanstack/react-query";
import { queryKeys } from "@/lib/query-keys";
import { gamesApi } from "@/services/api/gamesApi";
import { matchesApi } from "@/services/api/matchesApi";
import { playersApi } from "@/services/api/playersApi";
import { teamsApi } from "@/services/api/teamsApi";
import { tournamentsApi } from "@/services/api/tournamentsApi";

export function useDashboardData() {
  return useQuery({
    queryKey: queryKeys.dashboard,
    queryFn: async () => {
      const [games, players, teams, matches, tournaments] = await Promise.all([
        gamesApi.getAll(),
        playersApi.getAll(),
        teamsApi.getAll(),
        matchesApi.getAll(),
        tournamentsApi.getAll(),
      ]);

      return { games, players, teams, matches, tournaments };
    },
  });
}

