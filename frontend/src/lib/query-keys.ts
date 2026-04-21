export const queryKeys = {
  games: ["games"] as const,
  players: ["players"] as const,
  player: (id: number) => ["players", id] as const,
  teams: ["teams"] as const,
  team: (id: number) => ["teams", id] as const,
  matches: ["matches"] as const,
  match: (id: number) => ["matches", id] as const,
  tournaments: ["tournaments"] as const,
  tournament: (id: number) => ["tournaments", id] as const,
  dashboard: ["dashboard"] as const,
  search: (value: string) => ["search", value] as const,
};

