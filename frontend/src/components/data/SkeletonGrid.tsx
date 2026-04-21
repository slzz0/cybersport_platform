export function SkeletonGrid({ count = 6 }: { count?: number }) {
  return (
    <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
      {Array.from({ length: count }).map((_, index) => (
        <div
          key={index}
          className="animate-pulse rounded-[28px] border border-white/8 bg-white/[0.03] p-5"
        >
          <div className="mb-6 h-4 w-24 rounded-full bg-white/10" />
          <div className="mb-3 h-8 w-2/3 rounded-full bg-white/10" />
          <div className="mb-2 h-3 w-full rounded-full bg-white/10" />
          <div className="h-3 w-5/6 rounded-full bg-white/10" />
        </div>
      ))}
    </div>
  );
}

