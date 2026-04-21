import { mockDb } from "@/lib/mock-data";
import { apiConfig } from "@/services/api/config";
import { apiClient, withMockFallback } from "@/services/api/client";
import type { Match, MatchPayload } from "@/types/entities";

export const matchesApi = {
  getAll: () =>
    withMockFallback(
      async () => {
        const { data } = await apiClient.get<Match[]>(apiConfig.endpoints.matches);
        return data;
      },
      () => mockDb.getMatches(),
    ),

  getById: (id: number) =>
    withMockFallback(
      async () => {
        const { data } = await apiClient.get<Match>(`${apiConfig.endpoints.matches}/${id}`);
        return data;
      },
      () => {
        const match = mockDb.getMatchById(id);
        if (!match) {
          throw new Error("Match not found");
        }

        return match;
      },
    ),

  create: (payload: MatchPayload) =>
    withMockFallback(
      async () => {
        const { data } = await apiClient.post<Match>(apiConfig.endpoints.matches, payload);
        return data;
      },
      () => mockDb.createMatch(payload),
    ),

  update: (id: number, payload: MatchPayload) =>
    withMockFallback(
      async () => {
        const { data } = await apiClient.put<Match>(`${apiConfig.endpoints.matches}/${id}`, payload);
        return data;
      },
      () => mockDb.updateMatch(id, payload),
    ),

  delete: (id: number) =>
    withMockFallback(
      async () => {
        await apiClient.delete(`${apiConfig.endpoints.matches}/${id}`);
        return undefined;
      },
      () => {
        mockDb.deleteMatch(id);
        return undefined;
      },
    ),
};
