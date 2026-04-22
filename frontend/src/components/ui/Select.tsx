import { forwardRef } from "react";
import type { SelectHTMLAttributes } from "react";
import { ChevronDown } from "lucide-react";
import { cn } from "@/lib/cn";

interface Option {
  label: string;
  value: string | number;
}

interface SelectProps extends SelectHTMLAttributes<HTMLSelectElement> {
  label?: string;
  error?: string;
  options: Option[];
  placeholder?: string;
}

export const Select = forwardRef<HTMLSelectElement, SelectProps>(function Select(
  {
    label,
    error,
    className,
    options,
    placeholder,
    ...props
  },
  ref,
) {
  return (
    <label className="flex flex-col gap-2">
      {label ? <span className="text-sm font-medium text-white/80">{label}</span> : null}
      <span className="relative">
        <select
          ref={ref}
          className={cn(
            "h-12 w-full appearance-none rounded-2xl border border-white/10 bg-white/[0.03] px-4 text-sm text-white outline-none transition focus:border-accent/50 focus:bg-white/[0.05]",
            error && "border-danger/60",
            className,
          )}
          {...props}
        >
          {placeholder ? <option value="">{placeholder}</option> : null}
          {options.map((option) => (
            <option
              key={`${option.value}`}
              value={option.value}
              className="bg-[#0f141d]"
            >
              {option.label}
            </option>
          ))}
        </select>
        <ChevronDown className="pointer-events-none absolute right-4 top-1/2 h-4 w-4 -translate-y-1/2 text-white/75" />
      </span>
      {error ? <span className="text-xs text-danger">{error}</span> : null}
    </label>
  );
});
