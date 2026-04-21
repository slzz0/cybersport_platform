import type { LucideIcon } from "lucide-react";
import { ArrowUpRight } from "lucide-react";
import { Card } from "@/components/ui/Card";

interface StatCardProps {
  label: string;
  value: string;
  trend: string;
  icon: LucideIcon;
}

export function StatCard({ label, value, trend, icon: Icon }: StatCardProps) {
  return (
    <Card className="group overflow-hidden">
      <div className="mb-5 flex items-start justify-between">
        <div>
          <p className="text-[11px] uppercase tracking-[0.3em] text-white/35">{label}</p>
          <p className="mt-3 font-display text-[40px] font-bold tracking-wide text-white">{value}</p>
        </div>
        <div className="rounded-[16px] border border-white/10 bg-[#141920] p-3 text-accent transition group-hover:-translate-y-1">
          <Icon className="h-5 w-5" />
        </div>
      </div>
      <div className="flex items-center gap-2 text-sm text-white/55">
        <ArrowUpRight className="h-4 w-4 text-accent" />
        {trend}
      </div>
    </Card>
  );
}
