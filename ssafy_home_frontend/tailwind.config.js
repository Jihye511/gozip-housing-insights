module.exports = {
  content: ['./index.html', './src/**/*.{vue,js,ts,jsx,tsx}'],
  theme: {
    extend: {},
    fontFamily: {
      bm: ['"BMHannaPro"', 'sans-serif'],
    },
  },
  plugins: [require('daisyui')],
}
