const path = require("path");
const rootConfig = require("../../../tailwind.config.js");
const sharedUiConfig = require("../../web/shared_ui/tailwind.config.js");

/** @type {import('tailwindcss').Config} */
module.exports = {
  ...rootConfig,
  content: [
    ...sharedUiConfig.content,
    path.join(__dirname, "./public/index.html"),
    path.join(__dirname, "./src/**/*.{js,jsx,ts,tsx}"),
    // Need to compile tailwind classes for Rivet UI components that we use
    path.resolve(__dirname, "../../../node_modules/@skydio/rivet-ui/src/**/*.{js,ts,jsx,tsx}"),
  ],
};
