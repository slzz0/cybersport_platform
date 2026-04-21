import { Link } from "react-router-dom";
import { Compass } from "lucide-react";
import { Card } from "@/components/ui/Card";

export function NotFoundPage() {
  return (
    <Card className="flex min-h-[70vh] flex-col items-center justify-center text-center">
      <div className="rounded-[28px] border border-accent/20 bg-accent/10 p-5">
        <Compass className="h-10 w-10 text-accent" />
      </div>
      <p className="mt-8 text-[11px] uppercase tracking-[0.32em] text-white/35">404</p>
      <h2 className="mt-3 font-display text-6xl font-bold tracking-wide text-white">Off the bracket</h2>
      <p className="mt-4 max-w-xl text-sm text-white/55">
        This route is not part of the GRACEIT map. Head back to the dashboard and keep the circuit moving.
      </p>
      <Link
        to="/dashboard"
        className="mt-8 inline-flex h-11 items-center justify-center rounded-2xl bg-accent px-4 text-sm font-semibold text-white shadow-glow transition hover:bg-[#ff8145]"
      >
        Go to dashboard
      </Link>
    </Card>
  );
}
