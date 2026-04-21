import { NavLink } from "react-router-dom";
import {
  BarChart3,
  Home,
  LayoutDashboard,
  Search,
  Swords,
  Trophy,
  UserSquare2,
  UsersRound,
} from "lucide-react";
import { cn } from "@/lib/cn";
import { BrandMark } from "@/components/ui/BrandMark";
import type { NavItem } from "@/types/ui";

const items: Array<NavItem & { icon: typeof Home }> = [
  { label: "Home", href: "/", description: "Platform front page", icon: Home },
  { label: "Dashboard", href: "/dashboard", description: "Workspace command", icon: LayoutDashboard },
  { label: "Rank", href: "/rank", description: "Top players and teams", icon: BarChart3 },
  { label: "Players", href: "/players", description: "Roster management", icon: UserSquare2 },
  { label: "Teams", href: "/teams", description: "Lineups and squads", icon: UsersRound },
  { label: "Matches", href: "/matches", description: "Schedule and results", icon: Swords },
  { label: "Tournaments", href: "/tournaments", description: "Events and formats", icon: Trophy },
  { label: "Search", href: "/search", description: "Global platform search", icon: Search },
];

export function SidebarNav() {
  return (
    <aside className="hidden h-screen w-[268px] shrink-0 border-r border-white/6 bg-[#0a0d12] px-4 py-5 lg:flex lg:flex-col">
      <div className="mb-6 flex items-center gap-3 px-2">
        <div className="flex h-11 w-11 items-center justify-center rounded-[18px] bg-[#11151b] shadow-[inset_0_1px_0_rgba(255,255,255,0.04)]">
          <BrandMark className="h-6 w-6" />
        </div>
        <div>
          <p className="font-display text-[26px] font-bold tracking-[0.16em] text-white">GRACEIT</p>
          <p className="text-[10px] uppercase tracking-[0.28em] text-white/32">competitive platform</p>
        </div>
      </div>

      <div className="space-y-1.5">
        {items.map((item) => {
          const Icon = item.icon;
          return (
            <NavLink
              key={item.href}
              to={item.href}
              className={({ isActive }) =>
                cn(
                  "group flex items-center gap-3 rounded-[18px] px-4 py-3 transition",
                  isActive
                    ? "bg-[#171d24] text-white shadow-[inset_0_1px_0_rgba(255,255,255,0.03)]"
                    : "text-white/52 hover:bg-[#14191f] hover:text-white",
                )
              }
            >
              {({ isActive }) => (
                <>
                  <div className="relative">
                    <span
                      className={cn(
                        "absolute -left-4 top-1/2 h-9 w-[2px] -translate-y-1/2 rounded-full bg-transparent transition",
                        isActive && "bg-accent shadow-[0_0_12px_rgba(255,122,50,0.85)]",
                      )}
                    />
                    <Icon
                      className={cn(
                        "h-4 w-4 transition",
                        isActive ? "text-white" : "text-white/55 group-hover:text-accent",
                      )}
                    />
                  </div>
                  <div className="min-w-0">
                    <p className="text-[15px] font-semibold">{item.label}</p>
                    <p className="mt-0.5 text-[11px] text-white/28">{item.description}</p>
                  </div>
                </>
              )}
            </NavLink>
          );
        })}
      </div>

      <div className="mt-auto rounded-[20px] border border-white/8 bg-[#10151c] p-5">
        <div className="mb-4 flex items-center gap-3">
          <BrandMark variant="badge" className="h-10 w-10 rounded-[16px]" />
          <div>
            <p className="text-[11px] uppercase tracking-[0.26em] text-white/35">GRACEIT mode</p>
            <p className="mt-1 text-sm text-white/72">Play, rank, compete</p>
          </div>
        </div>
        <p className="mt-2 text-sm text-white/55">
          A denser left rail inspired by competitive queue platforms and modern tournament hubs.
        </p>
      </div>
    </aside>
  );
}
