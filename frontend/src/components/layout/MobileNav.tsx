import { NavLink } from "react-router-dom";
import { X } from "lucide-react";
import { useUiStore } from "@/store/ui-store";
import { cn } from "@/lib/cn";
import { BrandMark } from "@/components/ui/BrandMark";

const navItems = [
  { label: "Home", href: "/" },
  { label: "Dashboard", href: "/dashboard" },
  { label: "Rank", href: "/rank" },
  { label: "Players", href: "/players" },
  { label: "Teams", href: "/teams" },
  { label: "Matches", href: "/matches" },
  { label: "Tournaments", href: "/tournaments" },
  { label: "Search", href: "/search" },
];

export function MobileNav() {
  const open = useUiStore((state) => state.mobileNavOpen);
  const setOpen = useUiStore((state) => state.setMobileNavOpen);

  if (!open) {
    return null;
  }

  return (
    <div className="fixed inset-0 z-40 lg:hidden">
      <button
        type="button"
        className="absolute inset-0 bg-black/60 backdrop-blur-sm"
        onClick={() => setOpen(false)}
      />
      <aside className="absolute left-0 top-0 h-full w-[82vw] max-w-[340px] border-r border-white/10 bg-[#0b0f14] p-5 shadow-panel">
        <div className="mb-8 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="flex h-11 w-11 items-center justify-center rounded-[18px] bg-[#12171d]">
              <BrandMark className="h-5 w-5" />
            </div>
            <div>
              <p className="font-display text-3xl font-bold tracking-[0.12em] text-white">GRACEIT</p>
              <p className="text-xs uppercase tracking-[0.2em] text-white/40">navigation</p>
            </div>
          </div>
          <button
            type="button"
            className="rounded-2xl border border-white/10 p-2 text-white/70"
            onClick={() => setOpen(false)}
          >
            <X className="h-4 w-4" />
          </button>
        </div>
        <div className="space-y-2">
          {navItems.map((item) => (
            <NavLink
              key={item.href}
              to={item.href}
              onClick={() => setOpen(false)}
              className={({ isActive }) =>
                cn(
                  "block rounded-[18px] px-4 py-3 text-sm font-semibold transition",
                  isActive
                    ? "bg-[#171d24] text-white"
                    : "text-white/58 hover:bg-[#14191f] hover:text-white",
                )
              }
            >
              {item.label}
            </NavLink>
          ))}
        </div>
      </aside>
    </div>
  );
}
