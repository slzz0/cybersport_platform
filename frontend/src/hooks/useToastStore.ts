import { useUiStore } from "@/store/ui-store";

export function useToastStore() {
  const toasts = useUiStore((state) => state.toasts);
  const pushToast = useUiStore((state) => state.pushToast);
  const removeToast = useUiStore((state) => state.removeToast);

  return { toasts, pushToast, removeToast };
}
