package Dario;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;

/**
 *
 * @author dario
 */
public class Shader {

    private int shaderProgramId = -1;
    private int vertexShaderId = -1;
    private int fragmentShaderId = -1;

    public Shader(String VertexFilename, String FragmentFilename) {

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

    }

    public void activate() {
        // Utilizzo il mio programma
        ARBShaderObjects.glUseProgramObjectARB(shaderProgramId);
    }

    public void deactivate() {
        // Reset del programma
        ARBShaderObjects.glUseProgramObjectARB(0);
    }

    public static ByteBuffer getProgramCode(String filename) {
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

    public static ByteBuffer toByteString(String str, boolean isNullTerminated) {
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

    public static ByteBuffer toByteString(String str) {
        return Shader.toByteString(str, true);
    }
}
