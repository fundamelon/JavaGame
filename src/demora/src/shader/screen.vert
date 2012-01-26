// Shader semplice che non trasforma per niente il vertice e piglia il colore dalla texture
// Simple shader that doesn't do anything :)

void main(void)
{
  gl_TexCoord[0] = gl_MultiTexCoord0;
  gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;  
}