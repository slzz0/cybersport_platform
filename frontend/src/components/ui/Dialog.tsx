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
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-[#05070bcc] px-4 backdrop-blur-sm">
      <div className="absolute inset-0" onClick={onClose} aria-hidden="true" />
      <div
        className={cn(
          "relative z-10 w-full max-w-2xl rounded-[30px] border border-white/10 bg-[#101620] p-6 shadow-panel",
          className,
        )}
      >
        <div className="mb-5 flex items-start justify-between gap-4">
          <div>
            <h3 className="font-display text-3xl font-bold tracking-wide text-white">{title}</h3>
            {description ? (
              <p className="mt-2 max-w-xl text-sm text-white/55">{description}</p>
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

