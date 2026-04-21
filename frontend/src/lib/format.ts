import type { Match, Tournament } from "@/types/entities";

export function formatCompactNumber(value: number) {
  return new Intl.NumberFormat("en-US", {
    notation: "compact",
    maximumFractionDigits: 1,
  }).format(value);
}

export function formatDate(value: string) {
  return new Intl.DateTimeFormat("en-US", {
    year: "numeric",
    month: "short",
    day: "numeric",
  }).format(new Date(value));
}

export function formatDateTime(value: string) {
  return new Intl.DateTimeFormat("en-US", {
    year: "numeric",
    month: "short",
    day: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  }).format(new Date(value));
}

export function formatInputDateTime(value: string) {
  const date = new Date(value);
  const year = date.getFullYear();
  const month = `${date.getMonth() + 1}`.padStart(2, "0");
  const day = `${date.getDate()}`.padStart(2, "0");
  const hours = `${date.getHours()}`.padStart(2, "0");
  const minutes = `${date.getMinutes()}`.padStart(2, "0");

  return `${year}-${month}-${day}T${hours}:${minutes}`;
}

export function getTournamentStatus(tournament: Tournament) {
  const now = new Date();
  const start = new Date(tournament.startDate);
  const end = new Date(tournament.endDate);

  if (end < now) {
    return "Finished";
  }

  if (start > now) {
    return "Upcoming";
  }

  return "Active";
}

export function getMatchStatus(match: Match) {
  const now = Date.now();
  const playedAt = new Date(match.playedAt).getTime();

  if (playedAt > now) {
    return "Upcoming";
  }

  const deltaHours = Math.abs(now - playedAt) / (1000 * 60 * 60);
  if (deltaHours <= 2) {
    return "Live";
  }

  return "Finished";
}

export function getTrendTone(value: string) {
  if (value.toLowerCase().includes("up")) {
    return "text-success";
  }

  if (value.toLowerCase().includes("down")) {
    return "text-danger";
  }

  return "text-white/70";
}

