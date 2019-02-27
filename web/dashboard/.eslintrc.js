module.exports = {
  root: true,
  env: {
    node: true,
    browser: true
  },
  extends: ["plugin:vue/essential", "@vue/standard"],
  rules: {
    "no-console": process.env.NODE_ENV === "production" ? "error" : "off",
    "no-debugger": process.env.NODE_ENV === "production" ? "error" : "off",
    indent: ["error", 4, { SwitchCase: 1 }],
    quotes: ["error", "single"],
    semi: ["error", "always"],
    "no-new": "off",
    "no-unused-vars": "off",
    "no-tabs": "off",
    "no-mixed-spaces-and-tabs": "off",
    "x-invalid-end-tag": "off",
    "vue/no-parsing-error": [2, { "x-invalid-end-tag": false }]
  },
  parserOptions: {
    parser: "babel-eslint"
  }
};
