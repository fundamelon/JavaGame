uniform sampler2D tex;

void main(void)
{

  vec4 color = texture2D(tex, gl_TexCoord[0].st);

  float colore = 0.9;

  color.r = colore;
  color.g = colore;
  color.b = colore;

 // gl_FragColor = color;
}