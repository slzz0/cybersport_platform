declare const _default: {
    content: string[];
    theme: {
        extend: {
            fontFamily: {
                display: [string, string];
                body: [string, string];
            };
            colors: {
                background: string;
                panel: string;
                surface: string;
                border: string;
                accent: string;
                muted: string;
                success: string;
                warning: string;
                danger: string;
            };
            boxShadow: {
                panel: string;
                glow: string;
            };
            backgroundImage: {
                "hero-radial": string;
            };
            keyframes: {
                float: {
                    "0%, 100%": {
                        transform: string;
                    };
                    "50%": {
                        transform: string;
                    };
                };
                pulseLine: {
                    "0%, 100%": {
                        opacity: string;
                    };
                    "50%": {
                        opacity: string;
                    };
                };
            };
            animation: {
                float: string;
                pulseLine: string;
            };
        };
    };
    plugins: any[];
};
export default _default;
