const path = require("path");

// NOTE(sam): This second relay config is to generate (duplicate) relay artifacts for the development
// server to use when building with turbopack. We plan to blow away these duplicated artifacts
// from the rest of the file tree when we migrate to stable turbopack.
module.exports = {
  src: path.resolve(__dirname, "../.."),
  schema: path.resolve(__dirname, "../../../tools/cloud_api/schema.graphql"),
  exclude: ["**/node_modules/**", "**/__mocks__/**", "**/__generated__/**"],
  language: "typescript",
  // NOTE(sam): no artifactDirectory since turbopack experimental doesn't support it; artifacts will
  // be co-located next to the source files
  eagerEsModules: true,
  customScalars: { DateTime: "string" },
  typescriptExcludeUndefinedFromNullableUnion: true,
  schemaExtensions: [
    path.resolve(__dirname, "./src/app/(new)/o/[orgId]/(feature-flagged)/(real-time-ops)/_relay"),
  ],
};
