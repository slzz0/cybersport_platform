import type { ReactNode } from "react";

interface SectionHeadingProps {
  eyebrow?: string;
  title: string;
  description?: string;
  action?: ReactNode;
}

export function SectionHeading({
  eyebrow,
  title,
  description,
  action,
}: SectionHeadingProps) {
  return (
    <div className="flex flex-col gap-3 border-b border-white/6 pb-4 md:flex-row md:items-end md:justify-between">
      <div>
        {eyebrow ? (
          <p className="mb-2 text-[10px] uppercase tracking-[0.32em] text-accent/80">{eyebrow}</p>
        ) : null}
        <h2 className="font-display text-[30px] font-bold tracking-wide text-white">{title}</h2>
        {description ? <p className="mt-2 max-w-2xl text-sm text-white/85">{description}</p> : null}
      </div>
      {action}
    </div>
  );
}
