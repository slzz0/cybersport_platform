import { cn } from "@/lib/cn";

interface BrandMarkProps {
  variant?: "logo" | "badge";
  className?: string;
}

export function BrandMark({ variant = "logo", className }: BrandMarkProps) {
  if (variant === "badge") {
    return (
      <div
        className={cn(
          "flex h-12 w-12 items-center justify-center rounded-2xl border border-white/10 bg-[#ff7a3d] shadow-[0_14px_30px_rgba(255,122,61,0.22)]",
          className,
        )}
      >
        <svg
          viewBox="0 0 32 32"
          className="h-6 w-6"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
          aria-hidden="true"
        >
          <circle cx="16" cy="16" r="7.5" stroke="white" strokeWidth="2.2" />
          <path d="M16 5.5V10" stroke="white" strokeWidth="2.2" strokeLinecap="round" />
          <path d="M16 22V26.5" stroke="white" strokeWidth="2.2" strokeLinecap="round" />
          <path d="M26.5 16H22" stroke="white" strokeWidth="2.2" strokeLinecap="round" />
          <path d="M10 16H5.5" stroke="white" strokeWidth="2.2" strokeLinecap="round" />
        </svg>
      </div>
    );
  }

  return (
    <svg
      viewBox="0 0 48 48"
      className={cn("h-7 w-7", className)}
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
      aria-hidden="true"
    >
      <path d="M10 18L38 7.5V30.5L10 18Z" fill="#FF7A32" />
      <path d="M10 18H24.5L38 30.5H24L10 18Z" fill="#E45E1B" />
    </svg>
  );
}

