import type { PropsWithChildren } from "react";
import { MobileNav } from "@/components/layout/MobileNav";
import { SidebarNav } from "@/components/layout/SidebarNav";
import { Topbar } from "@/components/layout/Topbar";
import { Toaster } from "@/components/ui/Toaster";

export function AppShell({ children }: PropsWithChildren) {
  return (
    <div className="relative flex min-h-screen bg-background">
      <div className="pointer-events-none absolute inset-0 noise-overlay opacity-70" />
      <SidebarNav />
      <MobileNav />
      <div className="relative z-10 flex min-h-screen flex-1 flex-col">
        <Topbar />
        <main className="flex-1 px-4 py-5 sm:px-6">
          <div className="mx-auto flex w-full max-w-[1520px] flex-col gap-6">{children}</div>
        </main>
      </div>
      <Toaster />
    </div>
  );
}
