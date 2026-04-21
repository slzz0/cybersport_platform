import { createBrowserRouter, Outlet } from "react-router-dom";
import { AppShell } from "@/components/layout/AppShell";
import { DashboardPage } from "@/pages/DashboardPage";
import { HomePage } from "@/pages/HomePage";
import { MatchDetailsPage } from "@/pages/MatchDetailsPage";
import { MatchesPage } from "@/pages/MatchesPage";
import { NotFoundPage } from "@/pages/NotFoundPage";
import { PlayerDetailsPage } from "@/pages/PlayerDetailsPage";
import { PlayersPage } from "@/pages/PlayersPage";
import { RankPage } from "@/pages/RankPage";
import { SearchPage } from "@/pages/SearchPage";
import { TeamDetailsPage } from "@/pages/TeamDetailsPage";
import { TeamsPage } from "@/pages/TeamsPage";
import { TournamentDetailsPage } from "@/pages/TournamentDetailsPage";
import { TournamentsPage } from "@/pages/TournamentsPage";

function ShellLayout() {
  return (
    <AppShell>
      <Outlet />
    </AppShell>
  );
}

export const router = createBrowserRouter([
  {
    path: "/",
    element: <ShellLayout />,
    children: [
      { index: true, element: <HomePage /> },
      { path: "dashboard", element: <DashboardPage /> },
      { path: "rank", element: <RankPage /> },
      { path: "players", element: <PlayersPage /> },
      { path: "players/:playerId", element: <PlayerDetailsPage /> },
      { path: "teams", element: <TeamsPage /> },
      { path: "teams/:teamId", element: <TeamDetailsPage /> },
      { path: "matches", element: <MatchesPage /> },
      { path: "matches/:matchId", element: <MatchDetailsPage /> },
      { path: "tournaments", element: <TournamentsPage /> },
      { path: "tournaments/:tournamentId", element: <TournamentDetailsPage /> },
      { path: "search", element: <SearchPage /> },
      { path: "*", element: <NotFoundPage /> },
    ],
  },
]);
