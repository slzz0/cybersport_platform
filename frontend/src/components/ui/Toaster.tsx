import { useEffect } from "react";
import { CheckCircle2, AlertCircle, Info } from "lucide-react";
import { useToastStore } from "@/hooks/useToastStore";
import { cn } from "@/lib/cn";

const toneMap = {
  success: {
    icon: CheckCircle2,
    className: "border-success/20 bg-success/10 text-white",
  },
  danger: {
    icon: AlertCircle,
    className: "border-danger/20 bg-danger/10 text-white",
  },
  info: {
    icon: Info,
    className: "border-white/10 bg-white/10 text-white",
  },
};

export function Toaster() {
  const { toasts, removeToast } = useToastStore();

  useEffect(() => {
    if (!toasts.length) {
      return;
    }

    const timers = toasts.map((toast) =>
      window.setTimeout(() => removeToast(toast.id), 2800),
    );

    return () => {
      timers.forEach((timer) => window.clearTimeout(timer));
    };
  }, [toasts, removeToast]);

  return (
    <div className="pointer-events-none fixed right-4 top-4 z-[60] flex w-full max-w-sm flex-col gap-3">
      {toasts.map((toast) => {
        const tone = toneMap[toast.tone ?? "info"];
        const Icon = tone.icon;

        return (
          <div
            key={toast.id}
            className={cn(
              "pointer-events-auto rounded-2xl border p-4 shadow-panel backdrop-blur-xl",
              tone.className,
            )}
          >
            <div className="flex gap-3">
              <Icon className="mt-0.5 h-5 w-5 shrink-0" />
              <div>
                <p className="text-sm font-semibold">{toast.title}</p>
                {toast.description ? (
                  <p className="mt-1 text-xs text-white/70">{toast.description}</p>
                ) : null}
              </div>
            </div>
          </div>
        );
      })}
    </div>
  );
}

