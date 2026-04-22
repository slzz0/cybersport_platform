import { useLocation, useNavigate } from "react-router-dom";
import { Menu, Search } from "lucide-react";
import { Button } from "@/components/ui/Button";
import { useUiStore } from "@/store/ui-store";

const routeMeta: Record<string, { title: string; subtitle: string }> = {
  "/": {
    title: "GRACEIT HQ",
    subtitle: "Operate players, teams, tournaments and match flow from one competitive workspace.",
  },
  "/dashboard": {
    title: "Dashboard",
    subtitle: "Platform pulse, entity activity and quick actions in one command center.",
  },
  "/rank": {
    title: "Rank",
    subtitle: "Track the strongest players, teams, tournaments and hottest matchups across the platform.",
  },
  "/players": {
    title: "Players",
    subtitle: "Scout, sort, update and track roster talent across the platform.",
  },
  "/teams": {
    title: "Teams",
    subtitle: "Manage lineups, game disciplines and team-level performance signals.",
  },
  "/matches": {
    title: "Matches",
    subtitle: "Control upcoming fixtures, completed series and schedule intelligence.",
  },
  "/tournaments": {
    title: "Tournaments",
    subtitle: "Run the calendar, event windows and prize structure cleanly.",
  },
  "/search": {
    title: "Search",
    subtitle: "Find any player, team, tournament or match from one universal surface.",
  },
};

export function Topbar() {
  const navigate = useNavigate();
  const location = useLocation();
  const setMobileNavOpen = useUiStore((state) => state.setMobileNavOpen);
  const showSearchTrigger = location.pathname !== "/search";
  const meta = routeMeta[location.pathname] ?? {
    title: "GRACEIT",
    subtitle: "Focused tools for modern esports operations.",
  };

  return (
    <header className="sticky top-0 z-30 border-b border-white/6 bg-background/95 px-4 py-4 backdrop-blur-xl sm:px-6">
      <div className="flex flex-col gap-4 xl:flex-row xl:items-center xl:justify-between">
        <div className="flex items-start gap-3">
          <button
            type="button"
            className="rounded-2xl border border-white/10 p-3 text-white/70 transition hover:bg-white/5 lg:hidden"
            onClick={() => setMobileNavOpen(true)}
          >
            <Menu className="h-5 w-5" />
          </button>
          <div className="rounded-[18px] border border-white/8 bg-[#11161d] px-4 py-3">
            <p className="text-[10px] uppercase tracking-[0.32em] text-accent/85">GRACEIT command</p>
            <h1 className="mt-1 font-display text-[28px] font-bold tracking-wide text-white">{meta.title}</h1>
            <p className="mt-1 max-w-2xl text-sm text-white/48">{meta.subtitle}</p>
          </div>
        </div>
        <div className="flex flex-col gap-3 sm:flex-row">
          {showSearchTrigger ? (
            <Button
              variant="secondary"
              className="min-w-[270px] justify-start rounded-[18px] bg-[#11161d] px-4 text-left text-white/75 hover:bg-[#151b23] lg:hidden"
              onClick={() => navigate("/search")}
            >
              <Search className="h-4 w-4 shrink-0 text-white/42" />
              <span className="flex-1 text-sm">Search teams, players or events</span>
              <span className="rounded-lg border border-white/10 px-2 py-1 text-[10px] uppercase tracking-[0.22em] text-white/28">
                /
              </span>
            </Button>
          ) : null}
        </div>
      </div>
    </header>
  );
}
