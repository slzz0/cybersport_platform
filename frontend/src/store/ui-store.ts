import { create } from "zustand";
import type { ToastMessage } from "@/types/ui";

interface UiStoreState {
  mobileNavOpen: boolean;
  toasts: ToastMessage[];
  setMobileNavOpen: (value: boolean) => void;
  pushToast: (toast: Omit<ToastMessage, "id">) => void;
  removeToast: (id: string) => void;
}

export const useUiStore = create<UiStoreState>((set) => ({
  mobileNavOpen: false,
  toasts: [],
  setMobileNavOpen: (value) => set({ mobileNavOpen: value }),
  pushToast: (toast) =>
    set((state) => ({
      toasts: [
        ...state.toasts,
        {
          id: crypto.randomUUID(),
          ...toast,
        },
      ],
    })),
  removeToast: (id) =>
    set((state) => ({
      toasts: state.toasts.filter((toast) => toast.id !== id),
    })),
}));

