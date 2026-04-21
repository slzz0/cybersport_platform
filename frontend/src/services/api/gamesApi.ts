import { mockDb } from "@/lib/mock-data";
import { apiConfig } from "@/services/api/config";
import { apiClient, withMockFallback } from "@/services/api/client";
import type { Game } from "@/types/entities";

export const gamesApi = {
  getAll: () =>
    withMockFallback(
      async () => {
        const { data } = await apiClient.get<Game[]>(apiConfig.endpoints.games);
        return data;
      },
      () => mockDb.getGames(),
    ),
};
