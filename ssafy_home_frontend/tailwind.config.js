module.exports = {
  content: ['./index.html', './src/**/*.{vue,js,ts,jsx,tsx}'],
  theme: {
    extend: {},
    fontFamily: {
      bmjua: ['BMJUA'],
    },
  },
  plugins: [require('daisyui')],
}
