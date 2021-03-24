package blackDiamonds.envs;

import diamond2DGL.Environment;

public class Menu extends Environment {

    private String vertexShaderSource = "" +
            "   #type vertex\n" +
            "    #version 120?\n" +
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
    private String fragmentShaderSource = "" +
            "   #type fragment\n" +
            "    #version 120?\n" +
            "\n" +
            "    in vec4 fragColor;\n" +
            "\n" +
            "    out vec4 color;\n" +
            "\n" +
            "    void main() {\n" +
            "        color = fragColor;\n" +
            "    }";
    private int vertexID, fragmentID, shaderProgram;

    public Menu(String name) {
        super(name);
    }

    @Override
    public void init() {

    }

    @Override
    public void update(float dT) {

    }

    @Override
    public void render() {

    }
}
