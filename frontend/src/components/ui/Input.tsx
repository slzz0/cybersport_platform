import { forwardRef } from "react";
import type { InputHTMLAttributes } from "react";
import { cn } from "@/lib/cn";

interface InputProps extends InputHTMLAttributes<HTMLInputElement> {
  label?: string;
  hint?: string;
  error?: string;
}

export const Input = forwardRef<HTMLInputElement, InputProps>(function Input(
  { label, hint, error, className, ...props },
  ref,
) {
  return (
    <label className="flex flex-col gap-2">
      {label ? <span className="text-sm font-medium text-white/80">{label}</span> : null}
      <input
        ref={ref}
        className={cn(
          "h-12 rounded-2xl border border-white/10 bg-white/[0.03] px-4 text-sm text-white outline-none transition placeholder:text-white/30 focus:border-accent/50 focus:bg-white/[0.05]",
          error && "border-danger/60",
          className,
        )}
        {...props}
      />
      {error ? <span className="text-xs text-danger">{error}</span> : null}
      {!error && hint ? <span className="text-xs text-white/45">{hint}</span> : null}
    </label>
  );
});
