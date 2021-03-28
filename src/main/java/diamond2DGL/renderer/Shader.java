package diamond2DGL.renderer;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;

public class Shader {

    private int shaderProgramID;
    private boolean inUse = false;
    private String vertexSource;
    private String fragmentSource;
    private String path;

    /*
     * TODO -> Add new constructor for loading vertex and fragment shaders from two
     * */
    public Shader(String filePath) {
        this.path = filePath;
        try {
            String source = new String(Files.readAllBytes(Paths.get(filePath)));
            String[] sources = source.split("(#type)( )+([a-zA-Z])+");

            // Find the first pattern after #type
            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\r\n", index);
            String firstPattern = source.substring(index, eol).trim();
            // Find the second pattern after #type
            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\r\n", index);
            String secondPattern = source.substring(index, eol).trim();

            if (firstPattern.equals("vertex")) {
                vertexSource = sources[1];
            } else if (firstPattern.equals("fragment")) {
                fragmentSource = sources[1];
            } else {
                throw new IOException("Unexpected token '" + firstPattern + "'");
            }

            if (secondPattern.equals("vertex")) {
                vertexSource = sources[2];
            } else if (secondPattern.equals("fragment")) {
                fragmentSource = sources[2];
            } else {
                throw new IOException("Unexpected token '" + secondPattern + "'");
            }
        } catch (IOException e) {
            e.printStackTrace();
            assert false : "Error: Failed shader file opening for: '" + filePath + "'";
        }
    }

    public void compile() {
        int vertexID, fragmentID;
        // COMPILATION AND SHADER LINKING
        // Vertex shader load, compile and pass to the GPU
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);
        // Error checking
        int sucess = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (sucess == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.err.println("EROOR: " + path + ":\n\tVertex shader compilation failed.");
            System.err.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        // Fragment shader load, compile and pass to the GPU
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);
        // Error checking
        sucess = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (sucess == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.err.println("EROOR: " + path + ":\n\tFragment shader compilation failed.");
            System.err.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        // Link shader and check errors
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        sucess = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (sucess == GL_FALSE) {
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.err.println("EROOR: " + path + ":\n\tLinkage failed.");
            System.err.println(glGetProgramInfoLog(shaderProgramID, len));
            assert false : "";
        }
    }

    public void use() {
        if (!inUse) {
            inUse = true;
            glUseProgram(shaderProgramID);
        }
    }

    public void detach() {
        inUse = false;
        glUseProgram(0);
    }

    public void uploadMat4f(String name, Matrix4f mat) {
        int location = glGetUniformLocation(shaderProgramID, name);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat.get(matBuffer);
        glUniformMatrix4fv(location, false, matBuffer);
    }

    public void uploadVec4f(String name, Vector4f vec) {
        int location = glGetUniformLocation(shaderProgramID, name);
        use();
        glUniform4f(location, vec.x, vec.y, vec.z, vec.w);
    }

    public void uploadMat3f(String name, Matrix3f mat) {
        int location = glGetUniformLocation(shaderProgramID, name);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat.get(matBuffer);
        glUniformMatrix3fv(location, false, matBuffer);
    }

    public void uploadVec3f(String name, Vector3f vec) {
        int location = glGetUniformLocation(shaderProgramID, name);
        use();
        glUniform3f(location, vec.x, vec.y, vec.z);
    }

    public void uploadVec2f(String name, Vector2f vec) {
        int location = glGetUniformLocation(shaderProgramID, name);
        use();
        glUniform2f(location, vec.x, vec.y);
    }

    public void uploadFloat(String name, float value) {
        int location = glGetUniformLocation(shaderProgramID, name);
        use();
        glUniform1f(location, value);
    }

    public void uploadInt(String name, int value) {
        int location = glGetUniformLocation(shaderProgramID, name);
        use();
        glUniform1i(location, value);
    }

    public void uploadTexture(String name, int slot) {
        int location = glGetUniformLocation(shaderProgramID, name);
        use();
        glUniform1i(location, slot);
    }
}
