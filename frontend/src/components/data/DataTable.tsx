import type { ReactNode } from "react";
import { cn } from "@/lib/cn";

interface Column<T> {
  key: string;
  header: string;
  render: (row: T) => ReactNode;
  className?: string;
}

interface DataTableProps<T> {
  data: T[];
  columns: Column<T>[];
}

export function DataTable<T>({ data, columns }: DataTableProps<T>) {
  return (
    <div className="overflow-hidden rounded-[28px] border border-white/10 bg-panel/80 shadow-panel">
      <div className="overflow-x-auto">
        <table className="min-w-full divide-y divide-white/8 text-left">
          <thead className="bg-white/[0.02]">
            <tr>
              {columns.map((column) => (
                <th
                  key={column.key}
                  className="px-5 py-4 text-[11px] uppercase tracking-[0.24em] text-white/40"
                >
                  {column.header}
                </th>
              ))}
            </tr>
          </thead>
          <tbody className="divide-y divide-white/6">
            {data.map((row, rowIndex) => (
              <tr key={rowIndex} className="transition hover:bg-white/[0.025]">
                {columns.map((column) => (
                  <td
                    key={column.key}
                    className={cn("px-5 py-4 align-middle text-sm text-white/80", column.className)}
                  >
                    {column.render(row)}
                  </td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

