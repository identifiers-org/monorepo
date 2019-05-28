// Checks wether a color is light or dark depending on its HSP value.
// Entry MUST be a color in hex form: #000000
//
// Adapted from https://awik.io/determine-color-bright-dark-using-javascript/
//
export const getColorTone = function (color) {
  color = '0x' + color.slice(1);

  const r = color >> 16;
  const g = color >> 8 & 255;
  const b = color & 255;

  // HSP equation from http://alienryderflex.com/hsp.html
  const hsp = Math.sqrt(0.299 * (r * r) + 0.587 * (g * g) + 0.114 * (b * b));

  // Using the HSP value, determine whether the color is light or dark
  return hsp < 127.5 ? 'dark' : 'light';
}
