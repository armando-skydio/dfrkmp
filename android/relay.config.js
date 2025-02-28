const path = require("path");
// This should match next.config.mjs
// nextConfig.compiler.relay options
// See https://nextjs.org/docs/advanced-features/compiler#relay
module.exports = {
  // NOTE(sam): This is a bit of a hack to get the relay compiler to pick up fragment definitions
  // from the rivet-entities library. Apparently they got rid of the `include` option which might
  // have allowed us to more specifically target the folders we want.
  src: path.resolve(__dirname, "../.."),
  schema: path.resolve(__dirname, "../../../tools/cloud_api/schema.graphql"),
  exclude: ["**/node_modules/**", "**/__mocks__/**", "**/__generated__/**"],
  language: "typescript",
  artifactDirectory: path.resolve(__dirname, "./src/__generated__"),
  // Needed for vite plugin relay
  // See https://github.com/oscartbeaumont/vite-plugin-relay
  eagerEsModules: true,
  customScalars: { DateTime: "string" },
  typescriptExcludeUndefinedFromNullableUnion: true,
  schemaExtensions: [
    path.resolve(__dirname, "./src/app/(new)/o/[orgId]/(feature-flagged)/(real-time-ops)/_relay"),
  ],
};
