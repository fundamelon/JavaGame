uniform float width;
uniform float height;

void main(void)
{

 vec4 a = gl_Vertex;
 gl_TexCoord[0] = gl_MultiTexCoord0;

 gl_FrontColor = gl_Color;
 gl_Position = gl_ModelViewProjectionMatrix * a;
 

}