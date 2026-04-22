import type { PropsWithChildren, ReactNode } from "react";
import { X } from "lucide-react";
import { cn } from "@/lib/cn";

interface DialogProps {
  open: boolean;
  title: string;
  description?: string;
  onClose: () => void;
  footer?: ReactNode;
  className?: string;
}

export function Dialog({
  open,
  title,
  description,
  onClose,
  footer,
  className,
  children,
}: PropsWithChildren<DialogProps>) {
  if (!open) {
    return null;
  }

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center px-4 py-6">
      <div
        className="absolute inset-0 bg-[radial-gradient(circle_at_top,rgba(255,122,47,0.12),transparent_42%),linear-gradient(180deg,rgba(7,10,15,0.48),rgba(7,10,15,0.72))] backdrop-blur-[3px]"
        onClick={onClose}
        aria-hidden="true"
      />
      <div
        className={cn(
          "relative z-10 max-h-[calc(100vh-3rem)] w-full max-w-2xl overflow-y-auto rounded-[30px] border border-white/14 bg-[#101620f2] p-6 shadow-[0_30px_90px_rgba(0,0,0,0.48)]",
          className,
        )}
      >
        <div className="pointer-events-none absolute inset-x-8 top-0 h-px bg-gradient-to-r from-transparent via-white/40 to-transparent" />
        <div className="mb-5 flex items-start justify-between gap-4">
          <div>
            <h3 className="font-display text-3xl font-bold tracking-wide text-white">{title}</h3>
            {description ? (
              <p className="mt-2 max-w-xl text-sm text-white/85">{description}</p>
            ) : null}
          </div>
          <button
            type="button"
            className="rounded-2xl border border-white/10 p-2 text-white/60 transition hover:bg-white/5 hover:text-white"
            onClick={onClose}
          >
            <X className="h-4 w-4" />
          </button>
        </div>
        <div>{children}</div>
        {footer ? <div className="mt-6 flex justify-end gap-3">{footer}</div> : null}
      </div>
    </div>
  );
}
