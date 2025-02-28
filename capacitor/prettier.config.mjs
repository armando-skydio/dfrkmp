/**
 * NOTE: At this time, it's not possible to spread the base config values
 * into this one, so we will just copy and paste for now.
 *
 * https://github.com/prettier/prettier-vscode/issues/3066#issuecomment-1675730554
 */

const importOrder = [
  "<BUILTIN_MODULES>",
  "<THIRD_PARTY_MODULES>",
  "",
  "@skydio/(.*)$", // Skydio libraries
  "",
  "^(app|components|hooks|relay|state|styles|utils)(/.*)$", // Typescript aliases
  "",
  "^(?!.*[.]module.scss)[./].*$", // Everything relative to the current file
  "",
  ".module.scss$", // CSS Modules
  "",
  // All type imports should be in their own block
  "<TYPES>",
  "<TYPES>^@skydio",
  "<TYPES>^__generated__",
  "<TYPES>^(components|hooks|relay|state|styles|utils)",
  "<TYPES>^[.]",
];

/** @type {import("prettier").Config} */
const config = {
  /** Copied from base config @ root */
  printWidth: 100,
  tabWidth: 2,
  useTabs: false,
  semi: true,
  quoteProps: "as-needed",
  jsxSingleQuote: false,
  trailingComma: "es5",
  bracketSpacing: true,
  arrowParens: "avoid",

  /** Custom overrides vs base config @ root */
  plugins: ["prettier-plugin-tailwindcss", "@ianvs/prettier-plugin-sort-imports"],
  tailwindFunctions: [
    "cn",
    "tv",
    // classnames template literal, helps for multiline as needed
    "ctl",
    // The following should be migrated away from:
    "cs",
    "classNames",
    "classnames",
  ],
  importOrder,
  importOrderParserPlugins: ["typescript", "jsx", "decorators-legacy"],
  overrides: [
    {
      files: "*.d.ts",
      options: {
        importOrderParserPlugins: ['["typescript", { "dts": true }]', "decorators"],
      },
    },
    {
      files: "*.stories.tsx",
      options: {
        importOrder: ["^react$", ...importOrder],
      },
    },
  ],
};

export default config;
