const FALLBACK_STYLES: Record<string, { background: string; ring: string }> = {
  "counter-strike 2": {
    background: "linear-gradient(135deg, rgba(255,122,50,0.28), rgba(255,122,50,0.08))",
    ring: "rgba(255,122,50,0.28)",
  },
  "dota 2": {
    background: "linear-gradient(135deg, rgba(239,68,68,0.24), rgba(148,28,28,0.1))",
    ring: "rgba(239,68,68,0.24)",
  },
  valorant: {
    background: "linear-gradient(135deg, rgba(255,82,82,0.24), rgba(180,45,45,0.08))",
    ring: "rgba(255,82,82,0.24)",
  },
  "league of legends": {
    background: "linear-gradient(135deg, rgba(34,197,214,0.22), rgba(56,98,184,0.08))",
    ring: "rgba(34,197,214,0.24)",
  },
};

function normalizeTeamName(name: string) {
  return name.trim().toLowerCase();
}

function isGenericWord(word: string) {
  return [
    "team",
    "esports",
    "gaming",
    "club",
    "academy",
    "squad",
    "gg",
  ].includes(word);
}

function buildMonogram(name: string) {
  const parts = name
    .trim()
    .split(/\s+/)
    .map((part) => part.replace(/[^a-z0-9]/gi, ""))
    .filter(Boolean);

  const meaningfulParts = parts.filter((part) => !isGenericWord(part.toLowerCase()));
  const source = meaningfulParts.length ? meaningfulParts : parts;

  if (!source.length) {
    return "TM";
  }

  if (source.length === 1) {
    const token = source[0];

    if (/^[a-z]{3,}$/i.test(token)) {
      return token.slice(0, 2).toUpperCase();
    }

    return token.slice(0, 2).toUpperCase();
  }

  return source
    .slice(0, 2)
    .map((part) => part[0] ?? "")
    .join("")
    .toUpperCase();
}

export function getTeamBranding(name: string, gameName?: string) {
  const normalizedName = normalizeTeamName(name);
  const normalizedGame = gameName?.trim().toLowerCase() ?? "";
  const fallbackStyle = FALLBACK_STYLES[normalizedGame] ?? {
    background: "linear-gradient(135deg, rgba(255,255,255,0.1), rgba(255,255,255,0.04))",
    ring: "rgba(255,255,255,0.12)",
  };

  return {
    key: normalizedName,
    monogram: buildMonogram(name),
    background: fallbackStyle.background,
    ring: fallbackStyle.ring,
  };
}
