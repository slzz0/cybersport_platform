import {
  Search,
  Shield,
  Swords,
  Trophy,
  UserSquare2,
} from "lucide-react";
import { BrandMark } from "@/components/ui/BrandMark";

interface GlobalSearchPanelProps {
  value: string;
  onChange: (value: string) => void;
  activeTab: string;
  onTabChange: (value: string) => void;
}

const tabs = [
  { key: "all", label: "All Results", icon: Search },
  { key: "players", label: "Players", icon: UserSquare2 },
  { key: "teams", label: "Teams", icon: Shield },
  { key: "matches", label: "Matches", icon: Swords },
  { key: "tournaments", label: "Tournaments", icon: Trophy },
];

export function GlobalSearchPanel({
  value,
  onChange,
  activeTab,
  onTabChange,
}: GlobalSearchPanelProps) {
  return (
    <aside className="search-rail-glow rounded-[28px] border border-white/8 bg-[#090b0f] p-4 lg:sticky lg:top-24">
      <div className="mb-6 flex items-center gap-3 px-1">
        <div className="flex h-10 w-10 items-center justify-center rounded-[18px] bg-[#13161a]">
          <BrandMark className="h-5 w-5" />
        </div>
        <div>
          <p className="font-display text-2xl font-bold tracking-[0.12em] text-white">DISCOVER</p>
          <p className="text-[10px] uppercase tracking-[0.28em] text-white/30">entity search</p>
        </div>
      </div>

      <label className="relative block">
        <Search className="pointer-events-none absolute left-4 top-1/2 h-4 w-4 -translate-y-1/2 text-white/34" />
        <input
          value={value}
          onChange={(event) => onChange(event.target.value)}
          placeholder="Поиск"
          className="h-12 w-full rounded-2xl bg-white/[0.06] pl-11 pr-4 text-sm text-white outline-none transition placeholder:text-white/32 focus:bg-white/[0.08]"
        />
      </label>

      <div className="mt-6 space-y-1">
        {tabs.map((tab) => {
          const Icon = tab.icon;
          const isActive = activeTab === tab.key;

          return (
            <button
              key={tab.key}
              type="button"
              onClick={() => onTabChange(tab.key)}
              className={`flex w-full items-center gap-3 rounded-2xl px-4 py-3 text-left transition ${
                isActive
                  ? "bg-white/[0.07] text-white"
                  : "text-white/50 hover:bg-white/[0.04] hover:text-white"
              }`}
            >
              <Icon className={`h-4 w-4 ${isActive ? "text-white" : "text-white/46"}`} />
              <span className="text-[15px] font-medium">{tab.label}</span>
            </button>
          );
        })}
      </div>

      <div className="mt-8 rounded-[24px] border border-accent/14 bg-accent/6 p-4">
        <p className="text-[11px] uppercase tracking-[0.24em] text-accent/72">Quick note</p>
        <p className="mt-2 text-sm leading-6 text-white/56">
          One focused search surface for players, teams, matches and tournaments, without duplicating the same control
          across every layout zone.
        </p>
      </div>
    </aside>
  );
}
