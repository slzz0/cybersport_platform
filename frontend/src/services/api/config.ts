export const apiConfig = {
  baseURL: import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080",
  useMockFallback: import.meta.env.VITE_USE_MOCK_FALLBACK !== "false",
  endpoints: {
    games: "/api/games",
    players: "/api/players",
    teams: "/api/teams",
    matches: "/api/matches",
    tournaments: "/api/tournaments",
  },
};

