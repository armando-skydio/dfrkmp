const path = require("path");
const rootConfig = require("../../../postcss.config.js");

module.exports = {
  ...rootConfig,
  plugins: {
    ...rootConfig.plugins,
    tailwindcss: {
      ...rootConfig.plugins.tailwindcss,
      config: path.join(__dirname, "tailwind.config.js"),
    },
    autoprefixer: {},
  },
};
