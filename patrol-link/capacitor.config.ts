import { CapacitorConfig } from "@capacitor/cli";

const config: CapacitorConfig = {
  appId: "com.skydio.patrol_link",
  appName: "patrol-link",
  webDir: "dist",
  server: {
    url: "https://customer--main--hackathon--nacho-carnicero.coder.dev.skyd.io/", // Set to the URL where the app is served for prod, set to http://localhost:3000 for local development
    // url: "http://localhost:3000",
    cleartext: true,
  },
  plugins: {
    SplashScreen: {
      launchAutoHide: true,
    },
  },
};

export default config;
