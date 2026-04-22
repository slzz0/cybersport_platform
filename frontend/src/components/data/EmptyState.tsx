import type { ReactNode } from "react";
import { Ghost } from "lucide-react";
import { Card } from "@/components/ui/Card";

interface EmptyStateProps {
  title: string;
  description: string;
  action?: ReactNode;
}

export function EmptyState({ title, description, action }: EmptyStateProps) {
  return (
    <Card className="flex min-h-[240px] flex-col items-center justify-center text-center">
      <div className="mb-4 rounded-2xl border border-white/10 bg-white/5 p-4">
        <Ghost className="h-8 w-8 text-accent" />
      </div>
      <h3 className="font-display text-2xl font-bold tracking-wide text-white">{title}</h3>
      <p className="mt-3 max-w-md text-sm text-white/85">{description}</p>
      {action ? <div className="mt-6">{action}</div> : null}
    </Card>
  );
}

