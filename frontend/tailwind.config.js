export default {
    content: ["./index.html", "./src/**/*.{ts,tsx}"],
    theme: {
        extend: {
            fontFamily: {
                display: ["Rajdhani", "sans-serif"],
                body: ["Inter", "sans-serif"],
            },
            colors: {
                background: "rgb(var(--color-background) / <alpha-value>)",
                panel: "rgb(var(--color-panel) / <alpha-value>)",
                surface: "rgb(var(--color-surface) / <alpha-value>)",
                border: "rgb(var(--color-border) / <alpha-value>)",
                accent: "rgb(var(--color-accent) / <alpha-value>)",
                muted: "rgb(var(--color-muted) / <alpha-value>)",
                success: "rgb(var(--color-success) / <alpha-value>)",
                warning: "rgb(var(--color-warning) / <alpha-value>)",
                danger: "rgb(var(--color-danger) / <alpha-value>)",
            },
            boxShadow: {
                panel: "0 18px 60px rgba(0, 0, 0, 0.35)",
                glow: "0 0 0 1px rgba(255, 120, 45, 0.12), 0 30px 80px rgba(255, 101, 36, 0.18)",
            },
            backgroundImage: {
                "hero-radial": "radial-gradient(circle at top left, rgba(255,102,51,0.22), transparent 32%), radial-gradient(circle at top right, rgba(255,162,89,0.12), transparent 28%), linear-gradient(180deg, rgba(255,255,255,0.02), rgba(255,255,255,0))",
            },
            keyframes: {
                float: {
                    "0%, 100%": { transform: "translateY(0px)" },
                    "50%": { transform: "translateY(-6px)" },
                },
                pulseLine: {
                    "0%, 100%": { opacity: "0.45" },
                    "50%": { opacity: "1" },
                },
            },
            animation: {
                float: "float 5s ease-in-out infinite",
                pulseLine: "pulseLine 2.6s ease-in-out infinite",
            },
        },
    },
    plugins: [],
};
