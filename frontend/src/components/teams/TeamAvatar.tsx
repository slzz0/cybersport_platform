import { cn } from "@/lib/cn";
import { getTeamBranding } from "@/lib/team-branding";

interface TeamAvatarProps {
  name: string;
  gameName?: string;
  size?: "sm" | "md" | "lg" | "xl";
  className?: string;
}

const sizeClasses = {
  sm: "h-10 w-10 rounded-[12px] text-xs",
  md: "h-12 w-12 rounded-[14px] text-sm",
  lg: "h-14 w-14 rounded-[16px] text-base",
  xl: "h-20 w-20 rounded-[20px] text-xl",
};

export function TeamAvatar({
  name,
  gameName,
  size = "md",
  className,
}: TeamAvatarProps) {
  const branding = getTeamBranding(name, gameName);

  return (
    <div
      className={cn(
        "flex shrink-0 items-center justify-center overflow-hidden border border-white/10 bg-[#12161d] text-white shadow-[inset_0_1px_0_rgba(255,255,255,0.04)]",
        sizeClasses[size],
        className,
      )}
      style={{
        backgroundImage: branding.background,
        boxShadow: `inset 0 1px 0 rgba(255,255,255,0.04), 0 0 0 1px ${branding.ring}`,
      }}
      aria-label={`${name} team avatar`}
    >
      <span className="font-display font-bold tracking-[0.16em] text-white/92">{branding.monogram}</span>
    </div>
  );
}
