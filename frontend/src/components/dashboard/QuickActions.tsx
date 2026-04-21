import { Link } from "react-router-dom";
import { ChevronRight } from "lucide-react";
import { Card } from "@/components/ui/Card";

const actions = [
  { label: "Create player", href: "/players" },
  { label: "Launch team setup", href: "/teams" },
  { label: "Schedule match", href: "/matches" },
  { label: "Register tournament", href: "/tournaments" },
];

export function QuickActions() {
  return (
    <Card className="h-full">
      <p className="text-[11px] uppercase tracking-[0.3em] text-white/35">Quick actions</p>
      <h3 className="mt-3 font-display text-2xl font-bold text-white">Keep the queue moving</h3>
      <p className="mt-2 text-sm text-white/55">
        Jump into the main operations with short paths optimized for tournament flow.
      </p>
      <div className="mt-6 space-y-3">
        {actions.map((action) => (
          <Link
            key={action.label}
            to={action.href}
            className="flex items-center justify-between rounded-[16px] border border-white/8 bg-[#141920] px-4 py-3 text-sm text-white/80 transition hover:border-accent/20 hover:bg-[#191f27]"
          >
            {action.label}
            <ChevronRight className="h-4 w-4 text-white/40" />
          </Link>
        ))}
      </div>
    </Card>
  );
}
