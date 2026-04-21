import type { PropsWithChildren, ReactNode } from "react";
import { Button } from "@/components/ui/Button";
import { Dialog } from "@/components/ui/Dialog";

interface EntityDialogProps {
  open: boolean;
  title: string;
  description: string;
  confirmLabel: string;
  cancelLabel?: string;
  onClose: () => void;
  onSubmit: () => void;
  submitting?: boolean;
  footerExtra?: ReactNode;
}

export function EntityDialog({
  open,
  title,
  description,
  confirmLabel,
  cancelLabel = "Cancel",
  onClose,
  onSubmit,
  submitting,
  footerExtra,
  children,
}: PropsWithChildren<EntityDialogProps>) {
  return (
    <Dialog
      open={open}
      onClose={onClose}
      title={title}
      description={description}
      footer={
        <>
          {footerExtra}
          <Button variant="secondary" onClick={onClose}>
            {cancelLabel}
          </Button>
          <Button onClick={onSubmit} loading={submitting}>
            {confirmLabel}
          </Button>
        </>
      }
    >
      {children}
    </Dialog>
  );
}

