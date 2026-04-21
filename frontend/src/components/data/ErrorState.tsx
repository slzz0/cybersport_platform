import type { ReactNode } from "react";
import { AlertTriangle } from "lucide-react";
import { Card } from "@/components/ui/Card";

interface ErrorStateProps {
  title?: string;
  description: string;
  action?: ReactNode;
}

export function ErrorState({
  title = "Something went off the rails",
  description,
  action,
}: ErrorStateProps) {
  return (
    <Card className="border-danger/20 bg-danger/5">
      <div className="flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
        <div className="flex items-start gap-4">
          <div className="rounded-2xl border border-danger/25 bg-danger/10 p-3">
            <AlertTriangle className="h-5 w-5 text-danger" />
          </div>
          <div>
            <h3 className="font-display text-2xl font-bold tracking-wide text-white">{title}</h3>
            <p className="mt-2 text-sm text-white/65">{description}</p>
          </div>
        </div>
        {action}
      </div>
    </Card>
  );
}

