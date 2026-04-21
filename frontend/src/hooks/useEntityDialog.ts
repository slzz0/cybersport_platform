import { useState } from "react";

export function useEntityDialog<T>() {
  const [item, setItem] = useState<T | null>(null);
  const [isOpen, setIsOpen] = useState(false);

  return {
    item,
    isOpen,
    openCreate: () => {
      setItem(null);
      setIsOpen(true);
    },
    openEdit: (value: T) => {
      setItem(value);
      setIsOpen(true);
    },
    close: () => setIsOpen(false),
  };
}

