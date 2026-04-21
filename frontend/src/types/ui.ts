export interface NavItem {
  label: string;
  href: string;
  description: string;
}

export interface SearchResultGroup<T> {
  title: string;
  type: "players" | "teams" | "matches" | "tournaments";
  items: T[];
}

export interface ToastMessage {
  id: string;
  title: string;
  description?: string;
  tone?: "success" | "danger" | "info";
}

