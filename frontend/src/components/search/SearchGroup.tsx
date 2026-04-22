import type { ReactNode } from "react";
import { Card } from "@/components/ui/Card";

interface SearchGroupProps {
  title: string;
  count: number;
  children: ReactNode;
  accent?: boolean;
}

export function SearchGroup({ title, count, children, accent = false }: SearchGroupProps) {
  return (
    <Card className={accent ? "border-accent/15 bg-[#0d1015]" : "bg-[#0f1216]"}>
      <div className="mb-5 flex items-center justify-between">
        <h3 className="font-display text-[28px] font-bold tracking-wide text-white">{title}</h3>
        <span className="rounded-full border border-white/10 bg-white/[0.03] px-3 py-1 text-xs uppercase tracking-[0.22em] text-white/65">
          {count} results
        </span>
      </div>
      <div className="space-y-3">{children}</div>
    </Card>
  );
}
