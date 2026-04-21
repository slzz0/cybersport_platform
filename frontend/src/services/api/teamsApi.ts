import { mockDb } from "@/lib/mock-data";
import { apiConfig } from "@/services/api/config";
import { apiClient, withMockFallback } from "@/services/api/client";
import type { Team, TeamPayload } from "@/types/entities";

export const teamsApi = {
  getAll: () =>
    withMockFallback(
      async () => {
        const { data } = await apiClient.get<Team[]>(apiConfig.endpoints.teams);
        return data;
      },
      () => mockDb.getTeams(),
    ),

  getById: (id: number) =>
    withMockFallback(
      async () => {
        const { data } = await apiClient.get<Team>(`${apiConfig.endpoints.teams}/${id}`);
        return data;
      },
      () => {
        const team = mockDb.getTeamById(id);
        if (!team) {
          throw new Error("Team not found");
        }

        return team;
      },
    ),

  create: (payload: TeamPayload) =>
    withMockFallback(
      async () => {
        const { data } = await apiClient.post<Team>(apiConfig.endpoints.teams, payload);
        return data;
      },
      () => mockDb.createTeam(payload),
    ),

  update: (id: number, payload: TeamPayload) =>
    withMockFallback(
      async () => {
        const { data } = await apiClient.put<Team>(`${apiConfig.endpoints.teams}/${id}`, payload);
        return data;
      },
      () => mockDb.updateTeam(id, payload),
    ),

  delete: (id: number) =>
    withMockFallback(
      async () => {
        await apiClient.delete(`${apiConfig.endpoints.teams}/${id}`);
        return undefined;
      },
      () => {
        mockDb.deleteTeam(id);
        return undefined;
      },
    ),
};
