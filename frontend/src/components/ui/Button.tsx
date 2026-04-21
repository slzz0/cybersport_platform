import type { ButtonHTMLAttributes, PropsWithChildren } from "react";
import { LoaderCircle } from "lucide-react";
import { cn } from "@/lib/cn";

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: "primary" | "secondary" | "ghost" | "danger";
  loading?: boolean;
}

const variants: Record<NonNullable<ButtonProps["variant"]>, string> = {
  primary:
    "border border-accent/40 bg-accent text-white shadow-[0_12px_24px_rgba(255,110,50,0.18)] hover:bg-[#ff8145] focus-visible:ring-accent/50",
  secondary:
    "border border-white/10 bg-[#151a21] text-white hover:border-white/16 hover:bg-[#191f28] focus-visible:ring-white/20",
  ghost:
    "bg-transparent text-white/70 hover:bg-white/[0.04] hover:text-white focus-visible:ring-white/20",
  danger:
    "bg-danger/90 text-white hover:bg-danger focus-visible:ring-danger/40",
};

export function Button({
  children,
  className,
  variant = "primary",
  loading = false,
  disabled,
  ...props
}: PropsWithChildren<ButtonProps>) {
  return (
    <button
      className={cn(
        "inline-flex h-11 items-center justify-center gap-2 rounded-[18px] px-4 text-sm font-semibold transition duration-200 focus-visible:outline-none focus-visible:ring-2 disabled:cursor-not-allowed disabled:opacity-60",
        variants[variant],
        className,
      )}
      disabled={disabled || loading}
      {...props}
    >
      {loading ? <LoaderCircle className="h-4 w-4 animate-spin" /> : null}
      {children}
    </button>
  );
}
