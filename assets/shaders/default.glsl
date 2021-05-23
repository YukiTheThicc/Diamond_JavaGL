    #type vertex
    #version 330 core

    layout (location=0) in vec3 attrPos;
    layout (location=1) in vec4 attrColor;
    layout (location=2) in vec2 attrTexCoords;
    layout (location=3) in float attrTexId;

    uniform mat4 uProjection;
    uniform mat4 uView;

    out vec4 fragColor;
    out vec2 fragTexCoords;
    out float fragTexId;

    void main() {
        fragColor = attrColor;
        fragTexCoords = attrTexCoords;
        fragTexId = attrTexId;
        gl_Position = uProjection * uView * vec4(attrPos, 1.0);
    }

    #type fragment
    #version 330 core

    in vec4 fragColor;
    in vec2 fragTexCoords;
    in float fragTexId;

    uniform sampler2D uTextures[8];

    out vec4 color;

    void main() {
        if (fragTexId > 0) {
            int id = int(fragTexId);
            color = fragColor * texture(uTextures[id], fragTexCoords);
        } else {
            color = fragColor;
        }
    }