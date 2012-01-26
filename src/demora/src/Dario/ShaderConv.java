package Dario;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author dario
 */
public class ShaderConv {

    private int shaderProgramId = -1;
    private int vertexShaderId = -1;
    private int fragmentShaderId = -1;
    private int widthPos = -1;
    private int heightPos = -1;
    private Image img;

    public ShaderConv(String VertexFilename, String FragmentFilename, String imgPath) {
        try {
            // Tengo via la mia immagine
            img = new Image(imgPath);
        } catch (SlickException ex) {
            Logger.getLogger(ShaderConv.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Carico e compilo il VertexShader
        vertexShaderId = ARBShaderObjects.glCreateShaderObjectARB(ARBVertexShader.GL_VERTEX_SHADER_ARB);
        ARBShaderObjects.glShaderSourceARB(vertexShaderId, getProgramCode(VertexFilename));
        ARBShaderObjects.glCompileShaderARB(vertexShaderId);

        // Carico e compilo il FragmentShader
        fragmentShaderId = ARBShaderObjects.glCreateShaderObjectARB(ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
        ARBShaderObjects.glShaderSourceARB(fragmentShaderId, getProgramCode(FragmentFilename));
        ARBShaderObjects.glCompileShaderARB(fragmentShaderId);

        // Tiro insieme lo Shader, appiccicandoci il VertexShader ed il
        // Fragment Shader
        shaderProgramId = ARBShaderObjects.glCreateProgramObjectARB();
        ARBShaderObjects.glAttachObjectARB(shaderProgramId, vertexShaderId);
        ARBShaderObjects.glAttachObjectARB(shaderProgramId, fragmentShaderId);

        // Link e Validazione
        ARBShaderObjects.glLinkProgramARB(shaderProgramId);
        ARBShaderObjects.glValidateProgramARB(shaderProgramId);


        // Recupero la posizione del parametro chiamato "baseTexture"
        ARBShaderObjects.glUseProgramObjectARB(shaderProgramId);


        
        widthPos = ARBShaderObjects.glGetUniformLocationARB(shaderProgramId, toByteString("width"));
        heightPos = ARBShaderObjects.glGetUniformLocationARB(shaderProgramId, toByteString("height"));
        

        ARBShaderObjects.glUseProgramObjectARB(0);


    }

    public void update(int delta) {
        
    }

    public void render(float startx, float starty, float width, float height) {
        // Utilizzo il mio programma
        ARBShaderObjects.glUseProgramObjectARB(shaderProgramId);

        // Bind della texture
        img.bind();

        ARBShaderObjects.glUniform1fARB(widthPos, 1024);
        ARBShaderObjects.glUniform1fARB(heightPos, 768);

        GL11.glBegin(GL11.GL_QUADS);
            GL11.glColor3f(1.0f, 0.3f, 1.0f);
            GL11.glTexCoord2f(0.0f, 0.0f);
            //ARBVertexShader.glVertexAttrib1fARB(valuePos, 50.0f);
            GL11.glVertex3f(startx, starty, 0.0f);

            GL11.glColor3f(1.0f, 1.0f, 0.2f);
            GL11.glTexCoord2f(1.0f, 0.0f);
            //ARBVertexShader.glVertexAttrib1fARB(valuePos, 200.0f);
            GL11.glVertex3f(startx + width, starty, 0.0f);

            GL11.glColor3f(0.3f, 1.0f, 0.7f);
            GL11.glTexCoord2f(1.0f, 0.75f);
            //ARBVertexShader.glVertexAttrib1fARB(valuePos, -20.0f);
            GL11.glVertex3f(startx + width, starty + height, 0.0f);

            GL11.glColor3f(0.5f, 0.3f, 1.0f);
            GL11.glTexCoord2f(0.0f, 0.75f);
            //ARBVertexShader.glVertexAttrib1fARB(valuePos, 0.0f);
            GL11.glVertex3f(startx, starty + height, 0.0f);
        GL11.glEnd();

        

        // Reset del programma
        ARBShaderObjects.glUseProgramObjectARB(0);
    }

    private ByteBuffer getProgramCode(String filename) {
        InputStream fileInputStream = null;
        byte[] shaderCode = null;

        try {
            if (fileInputStream == null) {
                fileInputStream = new FileInputStream(filename);
            }
            DataInputStream dataStream = new DataInputStream(fileInputStream);
            dataStream.readFully(shaderCode = new byte[fileInputStream.available()]);
            fileInputStream.close();
            dataStream.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        ByteBuffer shaderPro = BufferUtils.createByteBuffer(shaderCode.length);

        shaderPro.put(shaderCode);
        shaderPro.flip();

        return shaderPro;
    }

    private ByteBuffer toByteString(String str, boolean isNullTerminated) {
        int length = str.length();
        if (isNullTerminated) {
            length++;
        }
        ByteBuffer buff = BufferUtils.createByteBuffer(length);
        buff.put(str.getBytes());

        if (isNullTerminated) {
            buff.put((byte) 0);
        }

        buff.flip();
        return buff;
    }

    private ByteBuffer toByteString(String str) {
        return toByteString(str, true);
    }
}
