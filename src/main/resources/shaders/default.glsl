    #type vertex
    #version 120?

    layout (location=0) in vec3 attrPos;
    layout (location=1) in vec4 attrColor;

    out vec4 fragColor;

    void main() {
        fragColor = attrColor;
        gl_Position = vec4(attrPos, 1.0);
    }

    #type fragment
    #version 120?

    in vec4 fragColor;

    out vec4 color;

    void main() {
        color = fragColor;
    }