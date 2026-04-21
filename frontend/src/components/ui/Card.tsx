import type { HTMLAttributes, PropsWithChildren } from "react";
import { cn } from "@/lib/cn";

interface CardProps extends HTMLAttributes<HTMLDivElement> {
  tone?: "default" | "accent";
}

export function Card({
  children,
  className,
  tone = "default",
  ...props
}: PropsWithChildren<CardProps>) {
  return (
    <div
      className={cn(
        "rounded-[20px] border border-white/8 bg-[#0c1016] p-5 shadow-[0_18px_36px_rgba(0,0,0,0.22)] transition duration-300",
        tone === "accent" && "border-accent/22 bg-gradient-to-br from-[#181312] to-[#0d1117]",
        className,
      )}
      {...props}
    >
      {children}
    </div>
  );
}
