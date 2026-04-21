import { mockDb } from "@/lib/mock-data";
import { apiConfig } from "@/services/api/config";
import { apiClient, withMockFallback } from "@/services/api/client";
import type { Player, PlayerPayload } from "@/types/entities";

export const playersApi = {
  getAll: () =>
    withMockFallback(
      async () => {
        const { data } = await apiClient.get<Player[]>(apiConfig.endpoints.players);
        return data;
      },
      () => mockDb.getPlayers(),
    ),

  getById: (id: number) =>
    withMockFallback(
      async () => {
        const { data } = await apiClient.get<Player>(`${apiConfig.endpoints.players}/${id}`);
        return data;
      },
      () => {
        const player = mockDb.getPlayerById(id);
        if (!player) {
          throw new Error("Player not found");
        }

        return player;
      },
    ),

  create: (payload: PlayerPayload) =>
    withMockFallback(
      async () => {
        const { data } = await apiClient.post<Player>(apiConfig.endpoints.players, payload);
        return data;
      },
      () => mockDb.createPlayer(payload),
    ),

  update: (id: number, payload: PlayerPayload) =>
    withMockFallback(
      async () => {
        const { data } = await apiClient.put<Player>(`${apiConfig.endpoints.players}/${id}`, payload);
        return data;
      },
      () => mockDb.updatePlayer(id, payload),
    ),

  delete: (id: number) =>
    withMockFallback(
      async () => {
        await apiClient.delete(`${apiConfig.endpoints.players}/${id}`);
        return undefined;
      },
      () => {
        mockDb.deletePlayer(id);
        return undefined;
      },
    ),
};
