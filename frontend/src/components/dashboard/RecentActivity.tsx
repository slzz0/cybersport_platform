import { Link } from "react-router-dom";
import { formatDateTime } from "@/lib/format";
import { Card } from "@/components/ui/Card";

interface ActivityItem {
  id: string;
  title: string;
  subtitle: string;
  href: string;
  timestamp: string;
}

export function RecentActivity({ items }: { items: ActivityItem[] }) {
  return (
    <Card className="h-full">
      <p className="text-[11px] uppercase tracking-[0.3em] text-white/75">Recent activity</p>
      <h3 className="mt-3 font-display text-2xl font-bold text-white">Latest additions</h3>
      <div className="mt-6 space-y-3">
        {items.map((item) => (
          <Link
            key={item.id}
            to={item.href}
            className="block rounded-[16px] border border-white/8 bg-[#141920] p-4 transition hover:border-white/15 hover:bg-[#191f27]"
          >
            <div className="flex items-center justify-between gap-4">
              <div>
                <p className="font-medium text-white">{item.title}</p>
                <p className="mt-1 text-sm text-white/70">{item.subtitle}</p>
              </div>
              <p className="text-xs text-white/75">{formatDateTime(item.timestamp)}</p>
            </div>
          </Link>
        ))}
      </div>
    </Card>
  );
}
