    #type vertex
    #version 330 core

    layout (location=0) in vec3 attrPos;
    layout (location=1) in vec4 attrColor;

    uniform mat4 uProjection;
    uniform mat4 uView;

    out vec4 fragColor;

    void main() {
        fragColor = attrColor;
        gl_Position = uProjection * uView * vec4(attrPos, 1.0);
    }

    #type fragment
    #version 330 core

    in vec4 fragColor;

    out vec4 color;

    void main() {
        color = fragColor;
    }