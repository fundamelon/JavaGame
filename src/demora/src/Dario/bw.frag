uniform sampler2D tex;

void main(void)
{

  vec4 color = texture2D(tex, gl_TexCoord[0].st);

  float4 colore = color.r * 0.3 + color.g * 0.59 + color.b * 0.11;

  color.r = colore;
  color.g = colore;
  color.b = colore;

  gl_FragColor = color;
}