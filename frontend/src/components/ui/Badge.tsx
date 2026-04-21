import type { PropsWithChildren } from "react";
import { cn } from "@/lib/cn";

interface BadgeProps {
  tone?: "neutral" | "accent" | "success" | "warning" | "danger";
  className?: string;
}

const tones: Record<NonNullable<BadgeProps["tone"]>, string> = {
  neutral: "border-white/10 bg-white/[0.04] text-white/70",
  accent: "border-accent/28 bg-accent/12 text-[#ffb58f]",
  success: "border-success/25 bg-success/10 text-[#9af0c4]",
  warning: "border-warning/25 bg-warning/10 text-[#ffd78b]",
  danger: "border-danger/25 bg-danger/10 text-[#ffb0b0]",
};

export function Badge({
  children,
  tone = "neutral",
  className,
}: PropsWithChildren<BadgeProps>) {
  return (
    <span
      className={cn(
        "inline-flex items-center rounded-[999px] border px-3 py-1 text-[11px] font-semibold uppercase tracking-[0.2em]",
        tones[tone],
        className,
      )}
    >
      {children}
    </span>
  );
}
