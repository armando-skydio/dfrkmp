import path from "path";
import { fileURLToPath } from "url";
import CopyWebpackPlugin from "copy-webpack-plugin";

const aircamRoot = path
  .dirname(fileURLToPath(import.meta.url))
  .replace(/cloud\/clients\/customer_portal.*/g, "");

const toBoolean = envVar => {
  if (envVar === undefined) {
    return false;
  }
  return envVar.toLowerCase() === "true";
};

/** @type {import('next').NextConfig} */
const nextConfig = {
  // React strict mode, a must have
  reactStrictMode: true,
  output: "export", // Setting output to export to enable static export (needed for capacitorJS)
  distDir: "dist", // Match output directory with the one that capacitorJS expects
  productionBrowserSourceMaps: toBoolean(process.env.GENERATE_SOURCEMAP),

  // Transpile monorepo packages
  // See https://nextjs.org/blog/next-13-1#built-in-module-transpilation-stable
  // See https://github.com/vercel/next.js/blob/canary/packages/next/src/server/config-shared.ts
  // See https://github.com/vercel/next.js/discussions/42136
  transpilePackages: [
    "@skydio/3d_viewer_lib",
    "@skydio/api_util",
    "@skydio/core",
    "@skydio/channels",
    "@skydio/common_util",
    "@skydio/flight_deck_client_js",
    "@skydio/lcm",
    "@skydio/math",
    "@skydio/nomad",
    "@skydio/param_util",
    "@skydio/pbtypes",
    "@skydio/react-map-interaction",
    "@skydio/react_util",
    "@skydio/redux_util",
    "@skydio/relay-toolkit",
    "@skydio/rivet-entities",
    "@skydio/rivet-ui",
    "@skydio/shared_ui",
    "@skydio/spaceplot",
    "@skydio/uri_util",
    // This is the new skybus pkg, the old one is coming in through api_util
    "@skydio/skybus-experimental",
    // Everything below is needed to add this to avoid a "SyntaxError: Cannot use import statement outside a module". See https://skydio.slack.com/archives/C03HPHZTZBN/p1687299535936269?thread_ts=1687280866.105129&cid=C03HPHZTZBN
    // "@babel/runtime",
    "@jdultra/threedtiles",
    "@ant-design/icons",
    "@ant-design/icons-svg",
    "rc-util",
    "rc-pagination",
    "rc-picker",
    "rc-notification",
    "rc-tooltip",
    "streamlit-component-lib",
  ],

  experimental: {
    // Prefer loading of ES Modules over CommonJS
    // See https://nextjs.org/blog/next-11-1#es-modules-support
    // See https://github.com/vercel/next.js/discussions/27876
    esmExternals: true,
    // Experimental monorepo support
    // See https://github.com/vercel/next.js/pull/22867
    // See https://github.com/vercel/next.js/discussions/26420
    externalDir: true,
  },

  compiler: {
    relay: {
      src: "../..",
      // TODO(sam): Re-add artifact directory in dev when we migrate to stable turbopack
      artifactDirectory: process.env.NODE_ENV !== "development" ? "./src/__generated__" : undefined,
      language: "typescript",
      eagerEsModules: true,
    },
  },

  // TODO (Sam, Alberto): Remove this once we stop exporting the NextJS app statically
  // See https://skydio.atlassian.net/browse/SW-57683
  images: {
    unoptimized: true,
  },

  async headers() {
    return [
      {
        source: "/(.*)",
        headers: [
          {
            key: "Strict-Transport-Security",
            value: "max-age=31536000; preload", // 1 year
          },
        ],
      },
    ];
  },
};

export default nextConfig;
