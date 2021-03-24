package blackDiamonds.envs;

import diamond2DGL.Environment;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Menu extends Environment {

    private String vertexShaderSource ="#version 330 core\n" +
            "\n" +
            "    layout (location=0) in vec3 attrPos;\n" +
            "    layout (location=1) in vec4 attrColor;\n" +
            "\n" +
            "    out vec4 fragColor;\n" +
            "\n" +
            "    void main() {\n" +
            "        fragColor = attrColor;\n" +
            "        gl_Position = vec4(attrPos, 1.0);\n" +
            "    }";

    private String fragmentShaderSource = "#version 330 core\n" +
            "\n" +
            "    in vec4 fragColor;\n" +
            "\n" +
            "    out vec4 color;\n" +
            "\n" +
            "    void main() {\n" +
            "        color = fragColor;\n" +
            "    }";

    private int vertexID, fragmentID, shaderProgram;
    private int vaoID, vboID, eboID;
    private float[] vertexArray = {
             0.5f, -0.5f, 0.0f,      1.0f, 0.0f, 0.0f, 1.0f, // Bottom right
            -0.5f,  0.5f, 0.0f,      0.0f, 1.0f, 0.0f, 1.0f, // Top left
             0.5f,  0.5f, 0.0f,      0.0f, 0.0f, 1.0f, 1.0f, // Top Right
            -0.5f, -0.5f, 0.0f,      1.0f, 1.0f, 0.0f, 1.0f, // Bottom Left
    };
    private int[] elementArray = {
            /*
                x       x


                x       x
             */
            2, 1, 0, // Top right triangle
            0, 1, 3, // Bottom left triangle
    };

    public Menu(String name) {
        super(name);
    }

    @Override
    public void init() {
        // COMPILATION AND SHADER LINKING
        // Vertex shader load, compile and pass to the GPU
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, vertexShaderSource);
        glCompileShader(vertexID);
        // Error checking
        int sucess = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (sucess == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.err.println("EROOR: 'defaultShader.glsl'\n\tVertex shader compilation failed.");
            System.err.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        // Fragment shader load, compile and pass to the GPU
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentShaderSource);
        glCompileShader(fragmentID);
        // Error checking
        sucess = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (sucess == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.err.println("EROOR: 'defaultShader.glsl'\n\tFragment shader compilation failed.");
            System.err.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        // Link shader and check errors
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram, fragmentID);
        glLinkProgram(shaderProgram);

        sucess = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if (sucess == GL_FALSE) {
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.err.println("EROOR: 'defaultShader.glsl'\n\tLinkage failed.");
            System.err.println(glGetProgramInfoLog(shaderProgram, len));
            assert false : "";
        }

        // Generation and sending of VAO, VBO, and EBO to the GPU
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO upload vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add vertex attr pointers
        int positionSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionSize + colorSize) * floatSizeBytes;

        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * floatSizeBytes);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update(float dT) {

    }

    @Override
    public void render() {
        //Bind shader program
        glUseProgram(shaderProgram);
        // Bind the VAO
        glBindVertexArray(vaoID);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);
        glUseProgram(0);
    }
}
