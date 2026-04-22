import { ChevronLeft, ChevronRight } from "lucide-react";
import { cn } from "@/lib/cn";

interface PaginationProps {
  page: number;
  totalPages: number;
  totalItems: number;
  pageSize: number;
  onPageChange: (page: number) => void;
  className?: string;
}

function getVisiblePages(page: number, totalPages: number) {
  if (totalPages <= 5) {
    return Array.from({ length: totalPages }, (_, index) => index + 1);
  }

  if (page <= 3) {
    return [1, 2, 3, 4, totalPages];
  }

  if (page >= totalPages - 2) {
    return [1, totalPages - 3, totalPages - 2, totalPages - 1, totalPages];
  }

  return [1, page - 1, page, page + 1, totalPages];
}

export function Pagination({
  page,
  totalPages,
  totalItems,
  pageSize,
  onPageChange,
  className,
}: PaginationProps) {
  if (totalItems <= pageSize) {
    return null;
  }

  const from = (page - 1) * pageSize + 1;
  const to = Math.min(page * pageSize, totalItems);
  const visiblePages = getVisiblePages(page, totalPages);

  return (
    <div className={cn("flex flex-col gap-4 rounded-[24px] border border-white/8 bg-white/[0.03] px-4 py-4 sm:flex-row sm:items-center sm:justify-between", className)}>
      <p className="text-sm text-white/85">
        Showing <span className="font-semibold text-white">{from}-{to}</span> of{" "}
        <span className="font-semibold text-white">{totalItems}</span>
      </p>
      <div className="flex flex-wrap items-center gap-2">
        <button
          type="button"
          className="inline-flex h-10 items-center justify-center gap-2 rounded-[16px] border border-white/10 bg-[#151a21] px-3 text-sm font-semibold text-white transition hover:border-white/16 hover:bg-[#191f28] disabled:cursor-not-allowed disabled:opacity-50"
          onClick={() => onPageChange(page - 1)}
          disabled={page === 1}
        >
          <ChevronLeft className="h-4 w-4" />
          Prev
        </button>
        {visiblePages.map((visiblePage, index) => {
          const previousPage = visiblePages[index - 1];
          const shouldShowGap = previousPage && visiblePage - previousPage > 1;

          return (
            <div key={visiblePage} className="flex items-center gap-2">
              {shouldShowGap ? <span className="px-1 text-sm text-white/45">...</span> : null}
              <button
                type="button"
                className={cn(
                  "inline-flex h-10 min-w-10 items-center justify-center rounded-[16px] border px-3 text-sm font-semibold transition",
                  visiblePage === page
                    ? "border-accent/40 bg-accent text-white shadow-[0_12px_24px_rgba(255,110,50,0.18)]"
                    : "border-white/10 bg-[#151a21] text-white hover:border-white/16 hover:bg-[#191f28]",
                )}
                onClick={() => onPageChange(visiblePage)}
              >
                {visiblePage}
              </button>
            </div>
          );
        })}
        <button
          type="button"
          className="inline-flex h-10 items-center justify-center gap-2 rounded-[16px] border border-white/10 bg-[#151a21] px-3 text-sm font-semibold text-white transition hover:border-white/16 hover:bg-[#191f28] disabled:cursor-not-allowed disabled:opacity-50"
          onClick={() => onPageChange(page + 1)}
          disabled={page === totalPages}
        >
          Next
          <ChevronRight className="h-4 w-4" />
        </button>
      </div>
    </div>
  );
}
