import { mockDb } from "@/lib/mock-data";
import { apiConfig } from "@/services/api/config";
import { apiClient, withMockFallback } from "@/services/api/client";
import type { Tournament, TournamentPayload } from "@/types/entities";

export const tournamentsApi = {
  getAll: () =>
    withMockFallback(
      async () => {
        const { data } = await apiClient.get<Tournament[]>(apiConfig.endpoints.tournaments);
        return data;
      },
      () => mockDb.getTournaments(),
    ),

  getById: (id: number) =>
    withMockFallback(
      async () => {
        const { data } = await apiClient.get<Tournament>(`${apiConfig.endpoints.tournaments}/${id}`);
        return data;
      },
      () => {
        const tournament = mockDb.getTournamentById(id);
        if (!tournament) {
          throw new Error("Tournament not found");
        }

        return tournament;
      },
    ),

  create: (payload: TournamentPayload) =>
    withMockFallback(
      async () => {
        const { data } = await apiClient.post<Tournament>(apiConfig.endpoints.tournaments, payload);
        return data;
      },
      () => mockDb.createTournament(payload),
    ),

  update: (id: number, payload: TournamentPayload) =>
    withMockFallback(
      async () => {
        const { data } = await apiClient.put<Tournament>(`${apiConfig.endpoints.tournaments}/${id}`, payload);
        return data;
      },
      () => mockDb.updateTournament(id, payload),
    ),

  delete: (id: number) =>
    withMockFallback(
      async () => {
        await apiClient.delete(`${apiConfig.endpoints.tournaments}/${id}`);
        return undefined;
      },
      () => {
        mockDb.deleteTournament(id);
        return undefined;
      },
    ),
};
