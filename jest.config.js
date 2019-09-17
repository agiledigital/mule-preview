module.exports = {
  collectCoverageFrom: ["**/*.{cljs,clj}", "!**/node_modules/**"],
  transformIgnorePatterns: ["/node_modules/", "/dist/"],
  collectCoverage: true
};
